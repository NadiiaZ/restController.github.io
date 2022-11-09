function showUserInfo(user) {
    let markup = '';
    let roles = '';
    let isAdm = false;
    user.userRoles.map(r => {
        if(r.roleName == 'ROLE_ADMIN') isAdm = true;
        roles += r.roleName + ' '
    })
    markup = `<tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.email}</td>
            <td>${roles}</td>                  
      </tr>`;
    document.getElementById('authUserInfo').innerHTML = markup;

    markup = `<span class="text-white font-weight-bold" text="Email">${user.email}</span>
        <span class="text-white"> with roles </span>
        <span class="text-white font-weight-bold" text="ROLES">${roles}</span>`;

    document.getElementById('userInfo').innerHTML = markup;

    if (isAdm) {
        start('http://localhost:8080/rest/admin')
            .then(data => {
                createTable(data)
            }).catch(reason => console.error(reason.message));
    }
}

function createTable(data) {
    let markup = ''
    for (const user of data) {
        let roles = ''

        user.userRoles.map(r => {
            roles += r.roleName + ' '
        })
        markup =
            `<tr id="tr${user.id}">
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.email}</td>
            <td>${roles}</td>                  
            <td><a class="btn btn-info"  onclick="getUserById(${user.id}, $(this).data('target'))" data-toggle="modal" data-target="#editModal">Edit</a></td>
            <td><a class="btn btn-danger"  onclick="getUserById(${user.id}, $(this).data('target'))" data-toggle="modal" data-target="#deleteModal">Delete</a></td>
        </tr>`;
        document.getElementById('userTable').innerHTML += markup;
    }

}

function checkValidation(field, fieldId) {
    if((field + '').length < 3 || (field + '').length > 30) {
        document.getElementById(fieldId).textContent
            = "This field should be greater than 2 and less 30";
        return true;
    }
}

function passEquels(pass1, pass2) {
    if(pass1 != pass2) {
        document.getElementById("passConfErr").textContent
            = "Passwords are different!";
        return false;
    }
}

function getDataFromForm(tagID, oper=null) {
    const inputs = document.querySelectorAll(`${tagID} input`);
    let username;
    let name;
    let surname;
    let email;
    let pass;
    let passConf;
    let valid = true;

    inputs.forEach(el => {
        if (el.id == "username")  username = el.value;
        if (el.id == "name")  name = el.value;
        if (el.id == "surname")  surname = el.value;
        if (el.id == "email")  email = el.value;
        if (el.id == "password")  pass = el.value;
        if (el.id == "passwordConf")  passConf = el.value;
    })

    // if (checkValidation(username, 'usernameErr')
    //     | checkValidation(name, 'nameErr')
    //     | checkValidation(surname, 'surnameErr')
    //     | checkValidation(pass, 'passErr')
    //     | (passEquels(pass, passConf) && (oper == "new" || oper == "regist" )))
    //     valid = false;

    let role = {}
    let roleArr = []

    if(oper != "regist")
    {
        document.querySelectorAll(`${tagID} option`).forEach(el=>
        {
            if (el.selected)
            {
                role = {
                    'id': el.value,
                    'roleName': el.text
                };
                roleArr.push(role);
            }
        })
    } else roleArr = [
        {'id': 1,
            'roleName': 'ROLE_USER'}
    ]

    const jsdata = {
        'name': name,
        'surname': surname,
        'username': username,
        'email': email,
        'password': pass,
        'userRoles': roleArr
    };
    if (!valid) return null;
    else
    {
        inputs.forEach(i=> {
            if(i.type != 'button')
                i.value = ""
        })
        return jsdata
    }
}

function addUser() {
    const jsonData = getDataFromForm("#addForm", "new");
    submitData(jsonData);
}

function registration() {
    const jsonData = getDataFromForm("#registerForm", "regist");
    submitData(jsonData);
}



function windowForm(id, data, tagID) {
    const inputs = document.querySelectorAll(`${tagID} input`);
    inputs.forEach(el => {
        if (el.id == "id") el.value = id;
        if (el.id == "name") el.value = data.name;
        if (el.id == "surname") el.value = data.surname;
        if (el.id == "email") el.value = data.email;
        if (el.id == "username") el.value = data.username;
        if (el.id == "password") el.value = data.password;

        const roles = []
        data.userRoles.map(e =>
        {
            roles.push(e.id)
        })
        document.querySelector(`${tagID} select`).innerHTML = `
            <option value="1" ${roles.includes(1) ? `selected` : ''}> ROLE_USER </option>
            <option value="2" ${roles.includes(2) ? `selected` : ''}> ROLE_ADMIN </option>
            `;
    })
}
