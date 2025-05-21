document.addEventListener('DOMContentLoaded', function() {
    loadJobSeekerData();

    const form = document.getElementById('jobSeekerForm');
    const photoUpload = document.getElementById('photoUpload');
    const photoPreview = document.getElementById('photoPreview');
    const cancelBtn = document.getElementById('cancelBtn');

    initEventListeners();

    function initEventListeners() {
        photoUpload.addEventListener('change', handlePhotoUpload);
        cancelBtn.addEventListener('click', cancelEdit);
        form.addEventListener('submit', submitForm);
    }

    function loadJobSeekerData() {
        const jobSeekerId = document.getElementById('jobSeekerId').value;
        fetch(`/jobseeker/getdata/${jobSeekerId}`)
            .then(response => response.json())
            .then(data => {
                populateForm(data);
            })
            .catch(error => {
                console.error('Ошибка загрузки данных соискателя:', error);
            });
    }

    function populateForm(data) {
        document.getElementById('jobSeekerId').value = data.id || '';
        document.getElementById('lastName').value = data.lastName || '';
        document.getElementById('firstName').value = data.firstName || '';
        document.getElementById('email').value = data.email || '';
        document.getElementById('phone').value = data.phone || '';
        document.getElementById('city').value = data.city || '';

        if (data.photoUrl) {
            photoPreview.src = data.photoUrl;
        }
    }

    function handlePhotoUpload(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                photoPreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    }

    function cancelEdit() {
        if (confirm('Вы уверены, что хотите отменить изменения?')) {
            window.location.href = '/account/job';
        }
    }

    function submitForm(event) {
        event.preventDefault();

        if (validateForm()) {
            const formData = new FormData(form);
            const jobSeekerId = document.getElementById('jobSeekerId').value;

            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch(`/jobseeker/update/${jobSeekerId}`, {
                method: 'POST',
                body: formData,
                headers: { [csrfHeader]: csrfToken }
            })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    } else if (!response.ok) {
                        throw new Error('Ошибка при сохранении профиля');
                    }
                })
                .catch(error => {
                    alert(error.message);
                });
        }
    }

    function validateForm() {
        let isValid = true;
        const requiredFields = [
            document.getElementById('lastName'),
            document.getElementById('firstName'),
            document.getElementById('email')
        ];

        requiredFields.forEach(field => {
            if (!field.value.trim()) {
                field.classList.add('error');
                isValid = false;
            } else {
                field.classList.remove('error');
            }
        });

        const email = document.getElementById('email');
        if (email.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            email.classList.add('error');
            alert('Пожалуйста, введите корректный email');
            isValid = false;
        }

        if (!isValid) {
            alert('Пожалуйста, заполните все обязательные поля');
        }

        return isValid;
    }
});