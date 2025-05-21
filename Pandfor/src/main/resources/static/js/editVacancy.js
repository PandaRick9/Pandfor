document.addEventListener('DOMContentLoaded', function() {
    loadCompanyData();

    const form = document.getElementById('companyForm');
    const logoUpload = document.getElementById('logoUpload');
    const logoPreview = document.getElementById('logoPreview');
    const cancelBtn = document.getElementById('cancelBtn');

    initEventListeners();

    function initEventListeners() {
        logoUpload.addEventListener('change', handleLogoUpload);
        cancelBtn.addEventListener('click', cancelEdit);
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            submitForm();
        });
    }

    function loadCompanyData() {
        fetch('/company/getdata')
            .then(response => {
                if (!response.ok) throw new Error('Ошибка загрузки данных компании');
                return response.json();
            })
            .then(data => populateForm(data))
            .catch(error => console.error('Error loading company data:', error));
    }

    function populateForm(data) {
        document.getElementById('companyId').value = data.companyId || '';
        document.getElementById('companyName').value = data.name || '';
        document.getElementById('description').value = data.description || '';
        document.getElementById('city').value = data.city || '';
        document.getElementById('email').value = data.email || '';
        document.getElementById('phone').value = data.phone || '';
        document.getElementById('website').value = data.website || '';

        if (data.logoUrl) {
            logoPreview.src = data.logoUrl;
        }
    }

    function handleLogoUpload(event) {
        const file = event.target.files[0];
        if (file) {
            if (!file.type.match('image.*')) {
                alert('Пожалуйста, выберите файл изображения');
                return;
            }

            const reader = new FileReader();
            reader.onload = function(e) {
                logoPreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    }

    function cancelEdit() {
        if (confirm('Вы уверены, что хотите отменить изменения?')) {
            window.location.href = '/company/profile';
        }
    }

    function submitForm() {
        if (!validateForm()) return;

        const formData = new FormData(form);
        const companyId = document.getElementById('companyId').value;
        const url = companyId ? `/company/update/${companyId}` : '/company/create';

        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        fetch(url, {
            method: 'POST',
            body: formData,
            headers: {
                [csrfHeader]: csrfToken,
                // 'Content-Type' не указываем явно, чтобы браузер сам установил boundary для FormData
            }
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                } else if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Ошибка сохранения компании')
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert('Данные компании успешно сохранены');
                    window.location.href = '/company/profile';
                } else {
                    throw new Error(data.message || 'Ошибка сохранения компании');
                }
            })
            .catch(err => {
                console.error('Error:', err);
                alert(err.message);
            });
    }

    function validateForm() {
        let isValid = true;
        const requiredFields = [
            document.getElementById('companyName'),
            // Добавьте другие обязательные поля при необходимости
        ];

        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                field.classList.add('error');
                isValid = false;
            } else {
                field.classList.remove('error');
            }
        });

        if (!isValid) {
            alert('Пожалуйста, заполните все обязательные поля');
        }

        // Дополнительная валидация email
        const email = document.getElementById('email').value;
        if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            alert('Пожалуйста, введите корректный email');
            isValid = false;
        }

        return isValid;
    }
});