function submitSeekerForm() {
    const seekerData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        city: document.getElementById('city').value,
        patronymic:  document.getElementById('patronymic').value
    };

    const formData = new FormData();
    formData.append('seekerData', new Blob([JSON.stringify(seekerData)], {
        type: 'application/json'
    }));

    const photoFile = document.getElementById('photo').files[0];
    if (photoFile) {
        formData.append('photo', photoFile);
    }

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/jobseeker/submit', {
        method: 'POST',
        headers: {
            [csrfHeader]: csrfToken
        },
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log('Профиль соискателя успешно создан!');
                if (response.redirected) {
                    window.location.href = response.url;
                }
            } else {
                console.error('Ошибка при создании профиля. Код: ', response.status);
                alert('Ошибка при создании профиля');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Произошла ошибка при отправке данных');
        });
}

function handleBackButton() {
    if (window.history.length > 1) {
        window.history.back();
    } else {
        window.location.href = '/';
    }
}