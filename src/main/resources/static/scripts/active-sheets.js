document.addEventListener('DOMContentLoaded', function() {
    const filterInput = document.getElementById('filterInput');
    const table = document.querySelector('.chucksTable');
    const rows = table.querySelectorAll('tbody tr');
    function formatDate(dateString) {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = String(date.getFullYear()).slice(2);
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${day}/${month}/${year} - ${hours}:${minutes}:${seconds}`;
    }
    rows.forEach(row => {
        const dateCell = row.querySelector('td:nth-child(7)');
        if (dateCell) {
            const originalDate = dateCell.textContent.trim();
            dateCell.textContent = formatDate(originalDate);
        }
    });
    filterInput.addEventListener('input', function() {
        const filterValue = filterInput.value.toLowerCase();
        rows.forEach(row => {
            const rowText = row.textContent.toLowerCase();
            row.style.display = rowText.includes(filterValue) ? '' : 'none';
        });
    });
});