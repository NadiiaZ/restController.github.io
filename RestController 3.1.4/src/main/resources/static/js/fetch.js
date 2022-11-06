
async function start(url) {
    const response = await fetch(url);
    if (response.status >= 400)
    {
        console.error(response.text())
    } else {
        const data = await response.json()
        return data
    }
}

function submitData(jsonData) {
    if (jsonData != null) {
        if (jsonData) {
            fetch('http://localhost:8080/rest/admin/new',
                {
                    method: "POST",
                    body: JSON.stringify(jsonData),
                    headers: {'Content-Type': 'application/json'}
                }).then(response => {
                return response.text()
            }).then(data => {
                console.log(data)

                $('#userTable').empty()
                let url = 'http://localhost:8080/rest/admin/authUser';

                start(url)
                    .then(data => {
                        showUserInfo(data);
                        return data;
                    })

                $('.nav-tabs a[href="allUsers"]').tab('show');

            }).catch(err => {
                console.error(err)
            })
        }
    }
}

async function getUserById(id, tagID) {
    const response = await fetch(`http://localhost:8080/rest/admin/${id}`);
    if (response.status >= 400)
    {
        console.error(response.text())
    } else {
        const data = await response.json()
        windowForm(id, data, tagID)
    }
}

function deleteUser() {
    const id = document.querySelector("#deleteModal input").value
    fetch (`http://localhost:8080/rest/admin/${id}`,
        {
            method:"DELETE"
        }).then(res => res.json())
        .then(data => {
            setTimeout(()=>{
                $('#deleteModal').modal('hide')
                $('#tr' + id).remove();
            }, 500)
            console.log(data)
        }).catch(err => {
        console.error(err)
    })
}

function updateUser() {

    const id = document.querySelector("#editForm input").value
    const jsonData = getDataFromForm("#editForm");
    console.log(jsonData)
    if (jsonData != null) {
        if (jsonData) {
            fetch(`http://localhost:8080/rest/admin/${id}`,
                {
                    method: "PATCH",
                    body: JSON.stringify(jsonData),
                    headers: {'Content-Type': 'application/json'}
                }).then(response => {
                return response.text()
            }).then(data => {

                setTimeout(()=>{
                    $('#editModal').modal('hide')

                    $('#userTable').empty()
                    //$('#tr' + id).remove();
                    let url = 'http://localhost:8080/rest/admin/authUser';

                    start(url)
                        .then(data => {
                            showUserInfo(data);
                            return data;
                        })

                }, 500)

                console.log(data)
            }).catch(err => {
                console.error(err)
            })
        }
    }
}
