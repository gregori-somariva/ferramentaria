document.addEventListener('DOMContentLoaded', function () {
    const entityNames = document.querySelectorAll('.entityName');

    // Hover image functionality
    entityNames.forEach(function (entityName) {
        entityName.addEventListener('mouseover', function () {
            const entityId = entityName.getAttribute('data-entity-id');
            const entityType = entityName.getAttribute('data-entity-type');
            const imgPath = `/imagem/${entityType}/${entityId}`;
            const tableRow = entityName.closest('tr');

            let imgElement = document.createElement('img');
            imgElement.src = imgPath;
            imgElement.classList.add('entityImage');

            imgElement.style.visibility = 'hidden';
            document.body.appendChild(imgElement);

            setTimeout(() => {
                const imgElementRect = imgElement.getBoundingClientRect();
                const tableRowRect = tableRow.getBoundingClientRect();
                const rowBottom = tableRowRect.bottom + window.scrollY;
                const rowTop = tableRowRect.top + window.scrollY;

                let imgElementTop;
                const imgElementLeft = imgElementRect.left + imgElementRect.width - imgElementRect.width;

                if (rowBottom + imgElementRect.height > window.innerHeight) {
                    imgElementTop = rowTop - imgElementRect.height - 10;
                } else {
                    imgElementTop = rowBottom + 10;
                }

                imgElement.style.left = `${imgElementLeft}px`;
                imgElement.style.top = `${imgElementTop}px`;

                imgElement.style.visibility = 'visible';
            }, 100);
        });

        entityName.addEventListener('mouseout', function () {
            const imgElement = document.querySelector('.entityImage');
            if (imgElement) {
                imgElement.remove();
            }
        });
    });

    // Confirm deletion
    const deactivateForms = document.querySelectorAll('.deactivateForm');
    deactivateForms.forEach(function (form) {
        form.addEventListener('submit', function (event) {
            const confirmation = confirm("Confirmar inativação?");
            if (!confirmation) {
                event.preventDefault();
            }
        });
    });

    const tabs = document.querySelectorAll('.tableTab');
    const tables = document.querySelectorAll('.tableWrapper');

    tables.forEach(table => {
        table.style.display = 'none';
    });

    if (tabs.length > 0) {
        tabs[0].classList.add('active'); 
        const targetId = tabs[0].getAttribute('data-target'); 
        const selectedTable = document.getElementById(targetId); 

        if (selectedTable) { 
            selectedTable.style.display = 'flex'
        }
    };

    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            tables.forEach(table => {
                table.style.display = 'none';
            });

            const targetId = tab.getAttribute('data-target');
            const selectedTable = document.getElementById(targetId);
            if (selectedTable) {
                selectedTable.style.display = 'flex';
            }
        });
    });

    function filterTableRows(inputElement, tableId) {
        const filterValue = inputElement.value.toLowerCase();
        const tableRows = document.querySelectorAll(`#${tableId} tbody tr`);
    
        tableRows.forEach(row => {
            const rowText = row.textContent.toLowerCase();
            row.style.display = rowText.includes(filterValue) ? '' : 'none';
        });
    }
    
    document.getElementById('chuckFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'chucksTable');
    });
    
    document.getElementById('toolFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'toolsTable');
    });
    
    document.getElementById('templateFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'templatesTable');
    });
    
    document.getElementById('viseFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'visesTable');
    });

    document.getElementById('glChuckFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'glChucksTable');
    });

    document.getElementById('yokeRingFilterInput').addEventListener('input', function() {
        filterTableRows(this, 'yokeRingsTable');
    });
});