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

function submitApplication(event) {
    event.preventDefault();
    const form = document.getElementById('applyForm');
    const formData = new FormData(form);

    fetch('/applications/submit', {
        method: 'POST',
        body: formData
    }).then(resp => {
        if (resp.ok) {
            console.log('Отклик отправлен');
            closeModal();
        } else {
            console.log('Ошибка при отправке отклика');
        }
    }).catch(err => {
        console.error(err);
        console.log('Ошибка сети');
    });
}
