const listContainer = document.querySelector('#service-list');
var table = document.createElement("TABLE");
var trHead = document.createElement("TR");
var thName = document.createElement("TH");
    thName.innerHTML = "Service Name";
var thUrl = document.createElement("TH");
    thUrl.innerHTML = "URL";
var thStatus = document.createElement("TH");
    thStatus.innerHTML = "Status";
var thCreatedOn = document.createElement("TH");
    thCreatedOn.innerHTML = "Added On";
let servicesRequest = new Request('/service');
fetch(servicesRequest)
.then(function(response) { return response.json(); })
.then(function(serviceList) {

    if(serviceList.length > 0){
        trHead.appendChild(thName);
        trHead.appendChild(thUrl);
        trHead.appendChild(thStatus);
        trHead.appendChild(thCreatedOn);

        table.appendChild(trHead);
    }
  serviceList.forEach((service, index) => {

    var trContent = document.createElement("TR");
    var tdName = document.createElement("TD");
        tdName.innerHTML = service.name;
        tdName.setAttribute("id", "text-url-name"+index);
    var tdUrl = document.createElement("TD");
        tdUrl.innerHTML = service.url;
        tdUrl.setAttribute("id", "text-url-value"+index);
    var tdStatus = document.createElement("TD");
        tdStatus.innerHTML = service.status;
    var tdCreatedOn = document.createElement("TD");
        tdCreatedOn.innerHTML = service.createDate;

    var editBtn = document.createElement("BUTTON");
        editBtn.innerHTML = "Edit";
        editBtn.setAttribute("id", "edit-service"+index);
    var tdEditBtn = document.createElement("TD");
        tdEditBtn.appendChild(editBtn);
        editBtn.onclick = evt => {
            var updateEntry = document.createElement("div");
            var nameLabel = document.createElement("label");
                nameLabel.innerHTML = "New URL";
            var editUrlVal = document.createElement("INPUT");
                editUrlVal.setAttribute("id", "edit-url-value"+index);
                editUrlVal.setAttribute("type", "text");
            updateEntry.appendChild(nameLabel);
            updateEntry.appendChild(editUrlVal);
            updateEntry.appendChild(editSaveBtn);
            var tdEditSave = document.createElement("TD");
            tdEditBtn.remove();
            tdDeleteBtn.remove();
            tdEditSave.appendChild(updateEntry);
            trContent.appendChild(tdEditSave);
            trContent.appendChild(tdDeleteBtn);
        }
    var editSaveBtn = document.createElement("BUTTON");
        editSaveBtn.innerHTML = "Save";
        editSaveBtn.setAttribute("id", "edit-service"+index);
        editSaveBtn.onclick = evt => {
            let urlName = document.querySelector('#text-url-name'+index).innerHTML;
            let urlVal = document.querySelector('#edit-url-value'+index).value;
            if(inValidUrl(urlVal)){
                alert("Invalid URL .... Please enter correct value!!");
            }else{
                fetch('/update', {
                method: 'post',
                headers: {
                'Accept': 'application/json, text/plain, */*',
                'Content-Type': 'application/json'
                },
                  body: JSON.stringify({url:urlVal, name:urlName})
                }).then(res=> location.reload());
            }
        }
    var deleteBtn = document.createElement("BUTTON");
        deleteBtn.innerHTML = "Delete";
        deleteBtn.setAttribute("id", "delete-service"+index);
    var tdDeleteBtn = document.createElement("TD");
        tdDeleteBtn.appendChild(deleteBtn);
        deleteBtn.onclick = evt => {
            let urlName = document.querySelector('#text-url-name'+index).innerHTML;
            fetch('/delete', {
            method: 'delete',
            headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
            },
          body: JSON.stringify({name:urlName})
        }).then(res=> location.reload());
        }

    trContent.appendChild(tdName);
    trContent.appendChild(tdUrl);
    trContent.appendChild(tdStatus);
    trContent.appendChild(tdCreatedOn);
    trContent.appendChild(tdEditBtn);
    trContent.appendChild(tdDeleteBtn);

    table.appendChild(trContent);
  });
});
listContainer.appendChild(table);
const saveButton = document.querySelector('#post-service');
saveButton.onclick = evt => {
    let urlName = document.querySelector('#url-name').value;
    let urlVal = document.querySelector('#url-val').value;
    if(inValidUrl(urlVal)){
        alert("Invalid URL .... Please enter correct value!!");
    }else{
        fetch('/create', {
        method: 'post',
        headers: {
        'Accept': 'application/json, text/plain, */*',
        'Content-Type': 'application/json'
        },
          body: JSON.stringify({name:urlName, url:urlVal, status:"UNKNOWN"})
        }).then(res=> location.reload());
    }
}
