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
    skillDiv.setAttribute('data-skill-name', skill);
    skillDiv.setAttribute('data-skill-level', level);

    skillDiv.innerHTML = `
        ${skill} — ${getSkillLevelText(level)}
        <img src="/images/close-icon.png" alt="Удалить" class="remove-icon" onclick="removeSkill(this)">
    `;

    document.getElementById('skillsList').appendChild(skillDiv);
    input.value = '';
    levelSelect.value = 'BASIC';
}

function getSkillLevelText(value) {
    switch (value) {
        case 'BASIC': return 'Базовый';
        case 'INTERMEDIATE': return 'Средний';
        case 'ADVANCED': return 'Продвинутый';
        default: return '';
    }
}

function removeSkill(element) {
    element.parentElement.remove();
}
function getCsrfToken() {
    const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
    return { token, header };
}
function submitForm() {
    const educationItems = Array.from(document.querySelectorAll('.education-item')).map(item => ({
        institution: item.querySelector('.schoolName').value,
        specialization: item.querySelector('.speciality').value,
        graduationDate: item.querySelector('.graduationYear').value,
    }));

    const skills = Array.from(document.querySelectorAll('.skill-item')).map(el => ({
        name: el.getAttribute('data-skill-name'),
        proficiencyLevel: el.getAttribute('data-skill-level'),
    }));

    const jsonPart = {
        title: document.getElementById('jobTitle').value,
        personalInfo: {
            gender: document.getElementById('gender').value,
            birthDate: document.getElementById('birthDate').value,
            workExperienceSummary: document.getElementById('workDescription').value,
        },
        educationList: educationItems,
        jobPreference: {
            schedule: document.getElementById('workSchedule').value,
            employmentType: document.getElementById('employmentType').value,
            workFormat: document.getElementById('workFormat').value,
            experienceYear: document.getElementById('experience').value,
            desiredSalary: document.getElementById('salary').value,
        },
        skills: skills
    };

    const formData = new FormData();
    formData.append('resumeData', new Blob([JSON.stringify(jsonPart)], { type: 'application/json' }));



    const { token, header } = getCsrfToken();

    fetch('/resume/submit', {
        method: 'POST',
        headers: {
            [header]: token
        },
        body: formData
    })
    .then(response => {
        if (response.ok) {
            console.log('Резюме успешно отправлено!');
            if (response.redirected) {
                window.location.href = response.url;
            }
        } else {
            console.log('Ошибка при отправке резюме. Код: ', response.status);
        }
    })
    .catch(error => {
        console.error('Ошибка:', error);
    });
}



function addEducation() {
    const container = document.getElementById('educationContainer');
    
    const newItem = document.createElement('div');
    newItem.className = 'education-item';
    newItem.innerHTML = `
        <label>Название учебного заведения</label>
        <input type="text" class="schoolName" placeholder="МГУ">

        <label>Специальность</label>
        <input type="text" class="speciality" placeholder="Программная инженерия">

        <label>Год окончания</label>
        <input type="number" class="graduationYear" placeholder="2024">
        
        <button type="button" onclick="removeEducation(this)">Удалить</button>
    `;

    container.appendChild(newItem);
}

function removeEducation(button) {
    button.parentElement.remove();
}

function handleBackButton() {
    const loginPages = ['/auth/login', '/auth/registration'];
    const referrer = document.referrer;

    if (!referrer) {
        window.location.href = '/job';
        return;
    }

    const referrerUrl = new URL(referrer);
    const refPath = referrerUrl.pathname;

    if (loginPages.includes(refPath)) {
        // Если пришли с /login или /registration → идём на главную
        window.location.href = '/job';
    } else if (window.history.length > 1) {
        // Иначе идём назад
        window.history.back();
    } else {
        // Если нет истории — тоже идём на главную
        window.location.href = '/job';
    }
}
