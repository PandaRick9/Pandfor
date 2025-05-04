document.addEventListener('DOMContentLoaded', function() {
  const dropdownButton = document.querySelector('.dropbtn');
  const dropdownContent = document.querySelector('.dropdown-content');

  dropdownButton.addEventListener('click', function(event) {
    dropdownContent.style.display = dropdownContent.style.display === 'block' ? 'none' : 'block';
  });

  document.addEventListener('click', function(event) {
    if (!dropdownButton.contains(event.target) && !dropdownContent.contains(event.target)) {
      dropdownContent.style.display = 'none';
    }
  });
});
