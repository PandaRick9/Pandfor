document.addEventListener('DOMContentLoaded', function() {
    loadResumeData();

    const form = document.getElementById('resume-form');
    const educationList = document.getElementById('education-list');
    const skillsContainer = document.getElementById('skills-container');
    const addEducationBtn = document.getElementById('add-education-btn');
    const addSkillBtn = document.getElementById('add-skill-btn');
    const cancelBtn = document.getElementById('cancel-btn');

    initEventListeners();

    function initEventListeners() {
        addEducationBtn.addEventListener('click', addEducation);
        addSkillBtn.addEventListener('click', addSkill);
        cancelBtn.addEventListener('click', cancelEdit);

        educationList.addEventListener('click', function(e) {
            if (e.target.classList.contains('remove-btn')) {
                removeEducation(e.target);
            }
        });

        skillsContainer.addEventListener('click', function(e) {
            if (e.target.classList.contains('skill-remove-btn')) {
                removeSkill(e.target);
            }
        });

        form.addEventListener('submit', function(e) {
            e.preventDefault();
            submitForm();
        });
    }

    function loadResumeData() {
        const resumeId = getResumeIdFromUrl();

        if (resumeId) {
            fetch(`/resume/getdata/${resumeId}`)
                .then(response => response.json())
                .then(data => populateForm(data))
                .catch(error => console.error('Error loading resume:', error));
        }
    }

    function getResumeIdFromUrl() {
        const pathParts = window.location.pathname.split('/');
        return pathParts[pathParts.length - 1];
    }

    function populateForm(resumeData) {
        document.getElementById('resume-id').value = resumeData.id || '';
        document.getElementById('resume-title').value = resumeData.title || '';
        document.getElementById('is-active').checked = resumeData.isActive || false;

        if (resumeData.jobSeeker) {
            document.getElementById('first-name').value = resumeData.jobSeeker.firstName || '';
            document.getElementById('last-name').value = resumeData.jobSeeker.lastName || '';
            document.getElementById('patronymic').value = resumeData.jobSeeker.patronymic || '';
            document.getElementById('city').value = resumeData.jobSeeker.city || '';
            document.getElementById('phone').value = resumeData.jobSeeker.phone || '';
            document.getElementById('email').value = resumeData.jobSeeker.email || '';
        }

        // Персональная информация
        if (resumeData.personalInfo) {
            document.getElementById('birth-date').value = resumeData.personalInfo.birthDate || '';
            document.getElementById('gender').value = resumeData.personalInfo.gender || 'MALE';
            document.getElementById('work-experience').value = resumeData.personalInfo.workExperienceSummary || '';

            // Образование
            if (resumeData.personalInfo.education && resumeData.personalInfo.education.length > 0) {
                resumeData.personalInfo.education.forEach(edu => {
                    addEducationToForm(edu);
                });
            }
        }

        // Предпочтения по работе
        if (resumeData.jobPreference) {
            document.getElementById('desired-salary').value = resumeData.jobPreference.desiredSalary || '';
            document.getElementById('schedule').value = resumeData.jobPreference.schedule || 'FIVE_TWO';
            document.getElementById('employment-type').value = resumeData.jobPreference.employmentType || 'FULL_TIME';
            document.getElementById('work-format').value = resumeData.jobPreference.workFormat || 'REMOTELY';
            document.getElementById('experience').value = resumeData.jobPreference.experienceYear || 'NO_EXPERIENCE';
        }

        // Навыки
        if (resumeData.resumeSkills && resumeData.resumeSkills.length > 0) {
            resumeData.resumeSkills.forEach(skill => {
                addSkillToForm(skill);
            });
        }
    }

    function addEducationToForm(education) {
        const container = document.getElementById('education-list');
        const index = container.children.length;

        const div = document.createElement('div');
        div.className = 'education-edit-item';
        div.innerHTML = `
            <button type="button" class="remove-btn">×</button>
            <input type="hidden" name="personalInfo.education[${index}].id" value="${education.id || ''}">
            <div class="form-grid">
                <div class="form-group">
                    <label>Учебное заведение</label>
                    <input type="text" name="personalInfo.education[${index}].institution" 
                           value="${education.institution || ''}" required>
                </div>
                <div class="form-group">
                    <label>Специальность</label>
                    <input type="text" name="personalInfo.education[${index}].specialization" 
                           value="${education.specialization || ''}" required>
                </div>
                <div class="form-group">
                    <label>Год окончания</label>
                    <input type="number" name="personalInfo.education[${index}].graduationDate" 
                           value="${education.graduationDate || ''}" required>
                </div>
            </div>
        `;
        container.appendChild(div);
    }

    function addSkillToForm(skill) {
        const container = document.getElementById('skills-container');
        const index = container.children.length;

        const div = document.createElement('div');
        div.className = 'skill-edit-item';
        div.innerHTML = `
            <input type="hidden" name="resumeSkills[${index}].id" value="${skill.id || ''}">
            <input type="hidden" name="resumeSkills[${index}].skillName" value="${skill.skillName || ''}">
            <span>${skill.skillName || ''}</span>
            <select name="resumeSkills[${index}].proficiencyLevel" class="skill-level-select">
                <option value="BASIC" ${skill.proficiencyLevel === 'BASIC' ? 'selected' : ''}>Базовый</option>
                <option value="INTERMEDIATE" ${skill.proficiencyLevel === 'INTERMEDIATE' ? 'selected' : ''}>Средний</option>
                <option value="ADVANCED" ${skill.proficiencyLevel === 'ADVANCED' ? 'selected' : ''}>Продвинутый</option>
            </select>
            <button type="button" class="skill-remove-btn">×</button>
        `;
        container.appendChild(div);
    }

    function addEducation() {
        const container = document.getElementById('education-list');
        const index = container.children.length;

        const div = document.createElement('div');
        div.className = 'education-edit-item';
        div.innerHTML = `
            <button type="button" class="remove-btn">×</button>
            <input type="hidden" name="personalInfo.education[${index}].id" value="">
            <div class="form-grid">
                <div class="form-group">
                    <label>Учебное заведение</label>
                    <input type="text" name="personalInfo.education[${index}].institution" required>
                </div>
                <div class="form-group">
                    <label>Специальность</label>
                    <input type="text" name="personalInfo.education[${index}].specialization" required>
                </div>
                <div class="form-group">
                    <label>Год окончания</label>
                    <input type="number" name="personalInfo.education[${index}].graduationDate" required>
                </div>
            </div>
        `;
        container.appendChild(div);
    }

    function removeEducation(btn) {
        btn.closest('.education-edit-item').remove();
        reindexEducationItems();
    }

    function reindexEducationItems() {
        const items = document.querySelectorAll('#education-list .education-edit-item');
        items.forEach((item, index) => {
            const inputs = item.querySelectorAll('input[name^="personalInfo.education["]');
            inputs.forEach(input => {
                input.name = input.name.replace(/personalInfo\.education\[\d+\]/, `personalInfo.education[${index}]`);
            });
        });
    }

    function addSkill() {
        const nameInput = document.getElementById('new-skill-name');
        const levelSelect = document.getElementById('new-skill-level');
        const container = document.getElementById('skills-container');
        const index = container.children.length;

        if (nameInput.value.trim()) {
            const div = document.createElement('div');
            div.className = 'skill-edit-item';
            div.innerHTML = `
                <input type="hidden" name="resumeSkills[${index}].id" value="">
                <input type="hidden" name="resumeSkills[${index}].skillName" value="${nameInput.value}">
                <span>${nameInput.value}</span>
                <select name="resumeSkills[${index}].proficiencyLevel" class="skill-level-select">
                    ${levelSelect.innerHTML}
                </select>
                <button type="button" class="skill-remove-btn">×</button>
            `;
            container.appendChild(div);
            nameInput.value = '';
        }
    }

    function removeSkill(btn) {
        btn.closest('.skill-edit-item').remove();
        reindexSkillItems();
    }

    function reindexSkillItems() {
        const items = document.querySelectorAll('#skills-container .skill-edit-item');
        items.forEach((item, index) => {
            const inputs = item.querySelectorAll('input[name^="resumeSkills["], select[name^="resumeSkills["]');
            inputs.forEach(input => {
                input.name = input.name.replace(/resumeSkills\[\d+\]/, `resumeSkills[${index}]`);
            });
        });
    }

    function cancelEdit() {
        if (confirm('Вы уверены, что хотите отменить изменения?')) {
            window.location.href = '/resumes';
        }
    }

    function submitForm() {
        if (validateForm()) {
            const formData = new FormData(form);
            const resumeId = document.getElementById('resume-id').value;
            const url = resumeId ? `/resume/update/${resumeId}` : '/resume/update';
            const method = 'POST';

            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch(url, {
                method: method,
                body: formData,
                headers: {
                    [csrfHeader]: csrfToken
                }
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


    function validateForm() {
        let isValid = true;
        const requiredFields = form.querySelectorAll('[required]');

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

        return isValid;
    }
});