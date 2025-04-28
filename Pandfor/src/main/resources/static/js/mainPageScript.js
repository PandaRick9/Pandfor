document.addEventListener('DOMContentLoaded', function() {
  const dropdownButton = document.querySelector('.dropbtn');
  const dropdownContent = document.querySelector('.dropdown-content');

  // Обработчик клика на кнопку
  dropdownButton.addEventListener('click', function(event) {
    // Переключаем видимость выпадающего меню
    dropdownContent.style.display = dropdownContent.style.display === 'block' ? 'none' : 'block';
  });

  // Закрыть выпадающее меню, если кликнули вне его
  document.addEventListener('click', function(event) {
    if (!dropdownButton.contains(event.target) && !dropdownContent.contains(event.target)) {
      dropdownContent.style.display = 'none';
    }
  });
});
