document.addEventListener('DOMContentLoaded', function () {

    const checkInputInDatalist = (inputId, datalistId) => {
        const inputField = document.getElementById(inputId);
        const inputValue = inputField.value.trim();
        const datalistOptions = document.getElementById(datalistId).options;
        const validOption = Array.from(datalistOptions).some(option => option.value === inputValue);

        let errorMessage = '';
        if (inputValue && !validOption) {
            if (inputId.includes('machine')) {
                errorMessage = `O valor "${inputValue}" não é uma opção válida para a máquina.`;
            } else if (inputId.includes('chuck') || inputId.includes('Chuck')) {
                errorMessage = `O valor "${inputValue}" não é uma opção válida para a castanha.`;
            } else if (inputId.includes('operation')) {
                errorMessage = `O valor "${inputValue}" não é uma opção válida para a operação.`;
            } else if (inputId.includes('tool')) {
                errorMessage = `O valor "${inputValue}" não é uma opção válida para a ferramenta.`;
            } else {
                errorMessage = `O valor "${inputValue}" não é válido para o campo.`;
            }
            inputField.classList.add('invalid');
            alert(errorMessage);
            return false;
        } else {
            inputField.classList.remove('invalid');
            return true;
        }
    };

    const checkToolPosition = (toolId, toolPositionId) => {
        const toolInput = document.getElementById(toolId);
        const toolPositionInput = document.getElementById(toolPositionId);
        const toolValue = toolInput.value.trim();
        const toolPositionValue = toolPositionInput.value.trim();

        if (toolValue && !toolPositionValue) {
            toolInput.classList.add('invalid');
            toolPositionInput.classList.add('invalid');
            alert(`Favor fornecer a posição da ferramenta: ${toolValue}`);
            return false;
        } else {
            toolInput.classList.remove('invalid');
            toolPositionInput.classList.remove('invalid');
            return true;
        }
    };

    const validateCycleTime = (inputId) => {
        const cycleTimeInput = document.getElementById(inputId);
        let cycleTimeValue = cycleTimeInput.value.trim();

        cycleTimeValue = cycleTimeValue.replace(/\D/g, '');

        if (cycleTimeValue.length <= 4) {
            cycleTimeValue = cycleTimeValue.replace(/(\d{2})(\d{2})$/, '$1:$2');
        } else if (cycleTimeValue.length === 6) {
            cycleTimeValue = cycleTimeValue.replace(/(\d{2})(\d{2})(\d{2})$/, '$1:$2:$3');
        }

        cycleTimeInput.value = cycleTimeValue;

        const cycleTimePattern = /^(?:\d{2}:\d{2}:\d{2}|\d{2}:\d{2})$/;

        if (!cycleTimePattern.test(cycleTimeValue)) {
            cycleTimeInput.classList.add('invalid');
            alert('O formato de tempo deve ser "00:00:00" ou "00:00".');
            return false;
        } else {
            cycleTimeInput.classList.remove('invalid');
            return true;
        }
    };

    const autoFillDescItemInput = () => {
        const itemInput = document.getElementById('itemInput');
        const itemDescription = document.getElementById('descItemInput');
        let itemValue = itemInput.value.trim();

        itemValue = itemValue.toUpperCase();
        itemInput.value = itemValue;

        const regex = /^PF(\d+)([a-zA-Z])(\d{1,2})$/;

        const match = itemValue.match(regex);
        
        if (match) {
            const numberAfterPF = match[1];
            const letterAfterNumber = match[2];
            const numberAfterLetter = match[3];

            itemDescription.value = `POLIA DE FERRO ${numberAfterPF} ${letterAfterNumber}${numberAfterLetter}`;
        }
    };

    document.getElementById('itemInput').addEventListener('blur', function() {
        autoFillDescItemInput();
    });

    document.getElementById('cycleTimeInput').addEventListener('blur', function() {
        validateCycleTime('cycleTimeInput');
    });

    document.getElementById('submit-form').addEventListener('click', function(event) {
        let isValid = true;

        isValid = isValid && checkInputInDatalist('machineInput', 'machineDatalist');
        isValid = isValid && checkInputInDatalist('chuckInput', 'chuckDatalist');
        isValid = isValid && checkInputInDatalist('secondChuckInput', 'chuckDatalist');
        isValid = isValid && checkInputInDatalist('operationInput', 'operationDatalist');
        isValid = isValid && checkInputInDatalist('templateInput', 'templateDatalist');
        isValid = isValid && checkInputInDatalist('viseInput', 'viseDatalist');
        isValid = isValid && checkInputInDatalist('glChuckInput', 'glChuckDatalist');
        isValid = isValid && checkInputInDatalist('yokeRingInput', 'yokeRingDatalist');

        for (let i = 1; i <= 10; i++) {
            isValid = isValid && checkInputInDatalist(`tool${i}Input`, 'toolDatalist');
        }

        for (let i = 2; i <= 10; i++) {
            isValid = isValid && checkToolPosition(`tool${i}Input`, `tool${i}Position`);
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
});
