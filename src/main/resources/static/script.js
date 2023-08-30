const fileList = document.getElementById("fileList");
const fileInput = document.getElementById("fileInput");
const actionList = document.getElementById("actionList");

function uploadFile() {
    const file = fileInput.files[0];
    if (!file) {
        alert("Please select a file.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("fileName", file.name);

    fetch("/files/upload", {
        method: "POST",
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);
        listFiles();
    })
    .catch(error => console.error("Upload error:", error));
}

function listFiles() {
    fetch("/files")
    .then(response => response.json())
    .then(data => {
        fileList.innerHTML = "";
//        actionList.innerHTML = "";

        data.forEach(file => {
            const li = document.createElement("li");
            li.innerHTML =
            `
                <a href="#" onclick="getFile(${file.id})">${file.fileName}</a>
                <button onclick="getFile(${file.id})">Download</button>
                <button onclick="openFileBrowserForUpdate(${file.id})">Update</button>
                <button onclick="deleteFile(${file.id})">Delete</button>
            `;
            fileList.appendChild(li);
//            actionList.appendChild(actions);
        });
    })
    .catch(error => console.error("Listing error:", error));
}

function openFileBrowserForUpdate(fileId) {
    const fileInput = document.getElementById("fileInput");
    fileInput.addEventListener("change", event => handleUpdateFileSelection(event, fileId));
    fileInput.click();
}

function handleUpdateFileSelection(event, fileId) {
    const selectedFile = event.target.files[0];

    const updateUrl = `/files/${fileId}`;
    const formData = new FormData();
    formData.append("file", selectedFile);

    fetch(updateUrl, {
        method: "PUT",
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        console.log("File updated:", data);
        listFiles();
    })
    .catch(error => {
        console.error("Update error:", error);
    });
}


function getFile(fileId) {
    fetch(`/files/${fileId}`)
    .then(response => response.blob())
    .then(blob => {
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.download = `file_${fileId}`;
        link.click();
    })
    .catch(error => console.error("Retrieve error:", error));
}


function deleteFile(fileId) {
    fetch(`/files/${fileId}`, {
        method: "DELETE"
    })
    .then(response => response.text())
    .then(data => {
        console.log(data);
        listFiles();
    })
    .catch(error => console.error("Delete error:", error));
}

listFiles();
