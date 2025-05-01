function nextStep(step) {
    document.querySelectorAll('.step').forEach(div => div.style.display = 'none');
    document.getElementById('step' + step).style.display = 'block';
}

function prevStep(step) {
    nextStep(step);
}
function addSkill() {
    const input = document.getElementById('skillInput');
    const levelSelect = document.getElementById('skillLevel');
    const skill = input.value.trim();
    const level = levelSelect.value;

    if (skill === '') return;

    const skillDiv = document.createElement('div');
    skillDiv.className = 'skill-item';
    skillDiv.innerHTML = `
        ${skill} — ${getSkillLevelText(level)}
        <img src="/static/images/close-icon.png" alt="Удалить" class="remove-icon" onclick="removeSkill(this)">
    `;

    document.getElementById('skillsList').appendChild(skillDiv);
    input.value = '';
    levelSelect.value = 'BASIC';
}
function getSkillLevelText(value) {
    switch (value) {
        case 'BASIC': return 'Начинающий';
        case 'INTERMEDIATE': return 'Средний';
        case 'ADVANCED': return 'Продвинутый';
        default: return '';
    }
}

function removeSkill(element) {
    element.parentElement.remove();
}

function submitForm() {
    const data = {
        jobTitle: document.getElementById('jobTitle').value,
        lastName: document.getElementById('lastName').value,
        firstName: document.getElementById('firstName').value,
        middleName: document.getElementById('middleName').value,
        gender: document.getElementById('gender').value,
        birthDate: document.getElementById('birthDate').value,
        phone: document.getElementById('phone').value,
        email: document.getElementById('email').value,
        city: document.getElementById('city').value,
        workDescription: document.getElementById('workDescription').value,
        schoolName: document.getElementById('schoolName').value,
        speciality: document.getElementById('speciality').value,
        graduationYear: document.getElementById('graduationYear').value,
        workSchedule: document.getElementById('workSchedule').value,
        employmentType: document.getElementById('employmentType').value,
        workFormat: document.getElementById('workFormat').value,
        experience: document.getElementById('experience').value,
        salary: document.getElementById('salary').value,
        skills: Array.from(document.querySelectorAll('.skill-item')).map(el => el.textContent.replace('✖', '').trim())
    };

    console.log('Отправка данных:', data);
    alert('Данные собраны и готовы к отправке!');
}
