document.addEventListener('DOMContentLoaded', () => {
    const imageContainers = document.querySelectorAll('.imageContainer');
    imageContainers.forEach(container => {
        container.style.pointerEvents = 'auto'; 
    });
    const saveButton = document.getElementById('save-button');
    if (saveButton) {
        saveButton.addEventListener('click', function(event) {
            event.preventDefault();
            window.print(); 
        });
    }
});
