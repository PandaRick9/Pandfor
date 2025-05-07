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
        case 'BASIC': return 'Начинающий';
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

function submitVacancy() {
    const skills = Array.from(document.querySelectorAll('.skill-item')).map(el => ({
        name: el.getAttribute('data-skill-name'),
        proficiencyLevel: el.getAttribute('data-skill-level'),
    }));

    const jsonPart = {
        title: document.getElementById('title').value,
        description: document.getElementById('description').value,
        salary: parseFloat(document.getElementById('salary').value),
        schedule: document.getElementById('schedule').value,
        employmentType: document.getElementById('employmentType').value,
        workFormat: document.getElementById('workFormat').value,
        requiredExperienceYears: document.getElementById('requiredExperienceYears').value,
        skills: skills
    };

    const { token, header } = getCsrfToken();

    fetch('/vacancy/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(jsonPart)
    })
    .then(response => {
        if (response.ok) {
            console.log('Вакансия успешно отправлена!');
            if (response.redirected) {
                window.location.href = response.url;
            }
        } else {
            console.log('Ошибка при отправке вакансии. Код: ', response.status);
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
        window.location.href = '/job';
        return;
    }

    const referrerUrl = new URL(referrer);
    const refPath = referrerUrl.pathname;

    if (loginPages.includes(refPath)) {
        window.location.href = '/job';
    } else if (window.history.length > 1) {
        window.history.back();
    } else {
        window.location.href = '/job';
    }
}
