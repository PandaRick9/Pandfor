function submitCompanyForm() {
    const jsonPart = {
        name: document.getElementById('companyName').value,
        description: document.getElementById('companyDescription').value,
        city: document.getElementById('companyCity').value,
        email:document.getElementById('companyEmail').value,
        phone:document.getElementById('companyPhone').value
    };

    const formData = new FormData();
    formData.append('companyData', new Blob([JSON.stringify(jsonPart)], { type: 'application/json' }));

    const photoFile = document.getElementById('photo').files[0];
    if (photoFile) {
        formData.append('photo', photoFile);
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/company/submit', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        },
        body: formData
    })
    .then(response => {
        if (response.ok) {
            console.log('Информация о компании успешно отправлена!');
            if (response.redirected) {
                window.location.href = response.url;
            }
        } else {
            console.error('Ошибка при отправке информации о компании. Код: ', response.status);
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}

function handleBackButton() {
    const loginPages = ['/auth/login', '/auth/registration'];
    const referrer = document.referrer;

    if (!referrer) {
        window.location.href = '/company';
        return;
    }

    const referrerUrl = new URL(referrer);
    const refPath = referrerUrl.pathname;

    if (loginPages.includes(refPath)) {
        // Если пришли с /login или /registration → идём на главную
        window.location.href = '/company';
    } else if (window.history.length > 1) {
        // Иначе идём назад
        window.history.back();
    } else {
        // Если нет истории — тоже идём на главную
        window.location.href = '/company';
    }
}