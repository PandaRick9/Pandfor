function openModal(btn) {
    const vacancyId = btn.getAttribute('data-id');
    const card = btn.closest('.vacancy-card');
    const title = card.querySelector('.vacancy-title').textContent;
    document.getElementById('modalJobTitle').textContent = title;
    document.getElementById('vacancyId').value = vacancyId;
    document.getElementById('applyModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('applyModal').style.display = 'none';
    document.getElementById('applyForm').reset();
}

function getCsrfToken() {
    const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
    return { token, header };
}



function submitApplication() {
    const coverLetter = document.getElementById('coverLetter').value;
    const vacancyId = document.getElementById('vacancyId').value;
    const resumeFile = document.getElementById('resumeFile').files[0];
    const selectedResumeId = document.getElementById('selectedResume').value;

    if (!coverLetter || !vacancyId) {
        console.log('Пожалуйста, заполните все поля формы.');
        return;
    }

    if (!resumeFile && !selectedResumeId) {
        console.log('Пожалуйста, выберите файл или сохранённое резюме.');
        return;
    }

    const jsonPart = {
        vacancyId: vacancyId,
        coverLetter: coverLetter,
        selectedResumeId: selectedResumeId
    };

    const formData = new FormData();
    formData.append('reactionData', new Blob([JSON.stringify(jsonPart)], { type: 'application/json' }));

    if (resumeFile) {
        formData.append('resumeFile', resumeFile);
    }

    const { token, header } = getCsrfToken();

    fetch('/reaction/submit', {
        method: 'POST',
        headers: {
            [header]: token
        },
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log('Отклик успешно отправлен!');
                closeModal();
            } else {
                console.log('Ошибка при отправке отклика. Код:', response.status);
            }
        })
        .catch(error => {
            console.error('Ошибка сети:', error);
        });
}
