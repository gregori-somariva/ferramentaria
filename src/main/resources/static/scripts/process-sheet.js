document.addEventListener('DOMContentLoaded', () => {
    const positions = {};
    const workspace = document.querySelector('.workspace');
    function initializeInteract(target) {
        const rotationHandle = target.querySelector('.rotation-handle');
        interact(target)
            .draggable({
                listeners: {
                    start(event) {
                        const id = event.target.getAttribute('id');
                        if (!positions[id]) {
                            positions[id] = { x: 0, y: 0, angle: 0 };
                        }
                    },
                    move(event) {
                        if (event.target !== rotationHandle) {
                            const target = event.target;
                            const id = target.getAttribute('id');
                            positions[id].x += event.dx;
                            positions[id].y += event.dy;
                            applyTransforms(target, id);
                        }
                    },
                },
                modifiers: [
                    interact.modifiers.snap({
                        targets: [interact.snappers.grid({ x: 5, y: 5 })],
                        range: Infinity,
                        relativePoints: [{ x: 0, y: 0 }],
                    }),
                    interact.modifiers.restrict({
                        restriction: 'parent',
                        elementRect: { top: 0, left: 0, bottom: 1, right: 1 },
                        endOnly: true,
                    }),
                ],
            })
            .resizable({
                edges: { left: false, right: true, bottom: true, top: false },
                listeners: {
                    move(event) {
                        const target = event.target;
                        const { width, height } = event.rect;
                        target.style.width = `${width}px`;
                        target.style.height = `${height}px`;
                        const img = target.querySelector('img');
                        if (img) {
                            img.style.width = '100%';
                            img.style.height = '100%';
                        }
                        applyTransforms(target, target.getAttribute('id'));
                    },
                },
                modifiers: [
                    interact.modifiers.restrictEdges({ outer: 'parent' }),
                    interact.modifiers.restrictSize({
                        min: { width: 50, height: 50 },
                        max: { width: 300, height: 500 },
                    }),
                ],
        });
    }
    function applyTransforms(element, id) {
        const pos = positions[id];
        element.style.transform = `translate(${pos.x}px, ${pos.y}px) rotate(${pos.angle}deg)`;
    }
    const existingImageContainers = document.querySelectorAll('.imageContainer');
    existingImageContainers.forEach(container => {
        initializeInteract(container);
    });
    function compressImage(file) {
        return new Promise((resolve, reject) => {
            const img = new Image();
            const reader = new FileReader();
            reader.onload = (e) => {
                img.src = e.target.result;
                img.onload = () => {
                    const canvas = document.createElement('canvas');
                    const ctx = canvas.getContext('2d');
                    const MAX_WIDTH = 800;
                    const MAX_HEIGHT = 800;
                    let width = img.width;
                    let height = img.height;
                    if (width > height) {
                        if (width > MAX_WIDTH) {
                            height *= MAX_WIDTH / width;
                            width = MAX_WIDTH;
                        }
                    } else {
                        if (height > MAX_HEIGHT) {
                            width *= MAX_HEIGHT / height;
                            height = MAX_HEIGHT;
                        }
                    }
                    canvas.width = width;
                    canvas.height = height;
                    ctx.drawImage(img, 0, 0, width, height);
                    canvas.toBlob((blob) => {
                        if (blob.size > 150 * 1024) {
                            canvas.toBlob((compressedBlob) => {
                                resolve(compressedBlob);
                            }, 'image/jpeg', 0.8);
                        } else {
                            resolve(blob);
                        }
                    }, 'image/jpeg', 0.8);
                };
                img.onerror = (err) => reject(err);
            };
            reader.readAsDataURL(file);
        });
    }
    const cameraButton = document.getElementById('cameraButton');
    const imageUpload = document.getElementById('imageUpload');
    cameraButton.addEventListener('click', () => {
        imageUpload.click();
    });
    imageUpload.addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            compressImage(file).then(compressedFile => {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const newImageContainer = document.createElement('div');
                    newImageContainer.classList.add('imageContainer');
                    newImageContainer.setAttribute('id', `image-${Date.now()}`);
                    newImageContainer.style.transform = 'translate(0, 0)';
                    newImageContainer.innerHTML = `<img src="${e.target.result}" alt="Uploaded Image" class="image">`;
                    workspace.appendChild(newImageContainer);
                    initializeInteract(newImageContainer);
                };
                reader.readAsDataURL(compressedFile);
            });
        }
    });
    const mainForm = document.getElementById('mainForm');
    const htmlDataInput = document.getElementById('htmlDataInput');
    function updateInputsInDOM() {
        const inputs = document.querySelectorAll('input');
        inputs.forEach(input => {
            if (input.type === 'text' || input.type === 'number') {
                input.setAttribute('value', input.value);
            }
        });
    }
    function modifyHTMLBeforeSubmission() {
        const clonedDocument = document.cloneNode(true);
        const clonedForm = clonedDocument.getElementById('mainForm');
        if (clonedForm) {
            clonedForm.remove();
        }
        const scriptTag = clonedDocument.querySelector('script[src="/scripts/process-sheet.js"]');
        if (scriptTag) {
            scriptTag.setAttribute('src', '/scripts/saved-process-sheet.js');
        }
        const interactScript = clonedDocument.querySelector('script[src="/scripts/interact.min.js"]');
        if (interactScript) {
            interactScript.remove();
        }
        const cameraButton = clonedDocument.getElementById('cameraButton');
        const imageUpload = clonedDocument.getElementById('imageUpload');
        if (cameraButton) cameraButton.remove();
        if (imageUpload) imageUpload.remove();
        const newSaveButton = clonedDocument.createElement('button');
        newSaveButton.id = 'save-button';
        newSaveButton.style.border = 'none';
        newSaveButton.style.background = 'none';
        newSaveButton.style.padding = '0';
        const newImage = clonedDocument.createElement('img');
        newImage.src = '/icons/download-button.png';
        newImage.alt = 'Salvar';
        newSaveButton.appendChild(newImage);
        clonedDocument.body.appendChild(newSaveButton);
        const clonedInputs = clonedDocument.querySelectorAll('input');
        clonedInputs.forEach(input => {
            input.setAttribute('readonly', 'true');
        });
        const serializer = new XMLSerializer();
        const htmlData = serializer.serializeToString(clonedDocument);
        if (htmlDataInput) {
            htmlDataInput.value = htmlData;
        }
    }
    mainForm.addEventListener('submit', function(event) {
        event.preventDefault();
        updateInputsInDOM();
        modifyHTMLBeforeSubmission();
        mainForm.submit();
    });
});
