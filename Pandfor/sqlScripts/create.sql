-- Компания
CREATE TABLE Company
(
    company_id  SERIAL PRIMARY KEY,   -- Уникальный идентификатор компании
    "name"      varchar(70) NOT NULL, -- Название компании
    description TEXT,                 -- Описание компании
    photo       varchar(255)--фото
);

-- Работодатель
CREATE TABLE Employer
(
    employer_id SERIAL PRIMARY KEY,                 -- Уникальный ID работодателя
    login       VARCHAR(100) NOT NULL UNIQUE,       -- Логин для входа
    "password"  VARCHAR(100) NOT NULL,              -- Пароль (хешированный)
    company_id  INT REFERENCES Company (company_id) -- Привязка к компании
);

-- Соискатель
CREATE TABLE JobSeeker
(
    seeker_id  SERIAL PRIMARY KEY,           -- Уникальный ID соискателя
    login      VARCHAR(100) NOT NULL UNIQUE, -- Логин пользователя
    "password" VARCHAR(100) NOT NULL         -- Пароль (хешированный)
);

-- Резюме
CREATE TABLE Resume
(
    resume_id    SERIAL PRIMARY KEY,                  -- Уникальный ID резюме
    seeker_id    INT REFERENCES JobSeeker (seeker_id),-- Привязка к соискателю
    title        VARCHAR(255),                        -- Название резюме (например, "Backend-разработчик")
    date_created TIMESTAMP,                           -- Дата создания
    is_active    BOOLEAN                              -- Активно ли резюме
);

-- Личная информация в резюме
CREATE TABLE PersonalInfo
(
    info_id                 SERIAL PRIMARY KEY,                       -- Уникальный ID личной информации
    resume_id               INT UNIQUE REFERENCES Resume (resume_id), -- Привязка к резюме (один к одному)
    first_name              VARCHAR(100),                             -- Имя
    last_name               VARCHAR(100),                             -- Фамилия
    patronymic              VARCHAR(100),                             -- Отчество
    birth_date              DATE,                                     -- Дата рождения
    phone                   VARCHAR(20),                              -- Телефон
    email                   VARCHAR(100),                             -- Email
    city                    VARCHAR(100),                             -- Город проживания
    gender                  VARCHAR(20),                              -- Пол
    "position"              VARCHAR(255),                             -- Желаемая должность
    photo                   varchar(255),                             --фото
    work_experience_summary TEXT                                      -- Краткое описание опыта работы
);

-- Образование
CREATE TABLE Education
(
    education_id    SERIAL PRIMARY KEY,                    -- Уникальный ID образования
    info_id         INT REFERENCES PersonalInfo (info_id), -- Привязка к личной информации
    institution     VARCHAR(255),                          -- Учебное заведение
    specialization  VARCHAR(255),                          -- Специализация/направление
    graduation_date DATE                                   -- Год окончания
);

-- Пожелания по условиям работы
CREATE TABLE JobPreferences
(
    preference_id   SERIAL PRIMARY KEY,                       -- Уникальный ID предпочтений
    resume_id       INT UNIQUE REFERENCES Resume (resume_id), -- Привязка к резюме
    schedule        VARCHAR(100),                             -- График работы (полный день, сменный и т.д.)
    employment_type VARCHAR(100),                             -- Тип занятости (полная, частичная, стажировка и т.д.)
    work_format     VARCHAR(100),                             -- Формат (удалённая, офис, гибрид)
    desired_salary  NUMERIC                                   -- Желаемая зарплата
);

-- Таблица скиллов (навыков)
CREATE TABLE Skill
(
    skill_id SERIAL PRIMARY KEY,          -- Уникальный ID навыка
    "name"   VARCHAR(100) UNIQUE NOT NULL -- Название навыка (например, Python)
);

-- Навыки в резюме
CREATE TABLE ResumeSkill
(
    resume_skill_id   SERIAL PRIMARY KEY,                                           -- Уникальный ID связи
    resume_id         INT NOT NULL REFERENCES Resume (resume_id) ON DELETE CASCADE, -- Привязка к резюме
    skill_id          INT NOT NULL REFERENCES Skill (skill_id) ON DELETE CASCADE,   -- Привязка к навыку
    proficiency_level VARCHAR(50),                                                  -- Уровень владения (начальный, средний, эксперт)

    CONSTRAINT uq_resume_skill UNIQUE (resume_id, skill_id)
);
-- Вакансии
CREATE TABLE Vacancy
(
    vacancy_id  SERIAL PRIMARY KEY,                    -- Уникальный ID вакансии
    employer_id INT REFERENCES Employer (employer_id), -- Автор вакансии
    company_id  INT REFERENCES Company (company_id),-- Компания, разместившая вакансию
    title       VARCHAR(255),                          -- Название должности
    description TEXT,                                  -- Описание работы
    salary      NUMERIC,                               -- Предлагаемая зарплата
    created_at  TIMESTAMP-- Дата публикации
);

-- Условия труда в вакансии
CREATE TABLE JobConditions
(
    condition_id              SERIAL PRIMARY KEY,                         -- Уникальный ID условий
    vacancy_id                INT UNIQUE REFERENCES Vacancy (vacancy_id), -- Привязка к вакансии
    schedule                  VARCHAR(100),                               -- График (полный день и т.д.)
    employment_type           VARCHAR(100),                               -- Тип занятости
    work_format               VARCHAR(100),                               -- Формат (офис, удалёнка и т.п.)
    required_experience_years INT                                         -- Необходимый опыт (в годах)
);

-- Навыки, требуемые для вакансии
CREATE TABLE VacancySkill
(
    vacancy_skill_id SERIAL PRIMARY KEY,                                             -- Уникальный ID связи
    vacancy_id       INT NOT NULL REFERENCES Vacancy (vacancy_id) ON DELETE CASCADE, -- Привязка к вакансии
    skill_id         INT NOT NULL REFERENCES Skill (skill_id) ON DELETE CASCADE,     -- Привязка к навыку
    required_level   VARCHAR(50),                                                    -- Требуемый уровень

    CONSTRAINT uq_vacancy_skill UNIQUE (vacancy_id, skill_id)
);
-- Отклики на вакансии
CREATE TABLE Reaction
(
    reaction_id  SERIAL PRIMARY KEY,                 -- Уникальный ID отклика
    resume_id    INT REFERENCES Resume (resume_id),  -- Резюме, с которого сделан отклик
    vacancy_id   INT REFERENCES Vacancy (vacancy_id),-- Вакансия, на которую откликнулись
    cover_letter TEXT,                               -- Сопроводительное письмо
    status       VARCHAR(50),                        -- Статус отклика (sent, viewed, rejected и т.п.)
    created_at   TIMESTAMP-- Дата отклика
);

-- Прикреплённые файлы к отклику
CREATE TABLE Attachment
(
    attachment_id SERIAL PRIMARY KEY,                    -- Уникальный ID файла
    reaction_id   INT REFERENCES Reaction (reaction_id), -- Привязка к отклику
    file_path     TEXT                                   -- Путь к файлу
);