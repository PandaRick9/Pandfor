document.addEventListener('DOMContentLoaded', function() {


    loadCompanyData();
    const form = document.getElementById('companyForm');
    const logoUpload = document.getElementById('logoUpload');
    const logoPreview = document.getElementById('logoPreview');
    const cancelBtn = document.getElementById('cancelBtn');


    logoUpload.addEventListener('change', handleLogoUpload);
    cancelBtn.addEventListener('click', cancelEdit);
    form.addEventListener('submit', submitForm);


    function loadCompanyData() {
        const companyId = document.getElementById('companyId').value;
        fetch(`/company/getdata/${companyId}`)
            .then(response => response.json())
            .then(data => {
                populateForm(data);
            })
            .catch(error => {
                console.error('Ошибка загрузки данных компании:', error);
            });
    }

    function populateForm(company) {
        const companyId = document.getElementById('companyId').value;
        document.getElementById('companyId').value = company.id || companyId;
        document.getElementById('companyName').value = company.name || '';
        document.getElementById('city').value = company.city || '';
        document.getElementById('description').value = company.description || '';
        document.getElementById('email').value = company.email || '';
        document.getElementById('phone').value = company.phone || '';

        if (company.logoUrl) {
            logoPreview.src = company.logoUrl;
        }

    }

    // Обработка загрузки логотипа
    function handleLogoUpload(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                logoPreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    }


    function cancelEdit() {
        if (confirm('Вы уверены, что хотите отменить изменения?')) {
            window.location.href = '/account/company';
        }
    }

    function submitForm(event) {
        event.preventDefault();

        if (validateForm()) {
            const formData = new FormData(form);
            const companyId = document.getElementById('companyId').value;

            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch( `/company/update/${companyId}` , {
                method: 'POST',
                body: formData,
                headers: { [csrfHeader]: csrfToken }
            })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    } else if (!response.ok) {
                        throw new Error('Ошибка при сохранении резюме');
                    }
                })
                .catch(error => {
                    alert(error.message);
                });
        }
    }

    // Валидация формы
    function validateForm() {
        let isValid = true;
        const companyName = document.getElementById('companyName');

        if (!companyName.value.trim()) {
            companyName.classList.add('error');
            isValid = false;
            alert('Пожалуйста, укажите название компании');
        } else {
            companyName.classList.remove('error');
        }

        return isValid;
    }
});