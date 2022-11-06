let url = 'http://localhost:8080/rest/admin/authUser';

start(url)
    .then(data => {
        showUserInfo(data);
        return data;
    })
