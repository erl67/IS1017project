(function() {
  const form = document.querySelector('form');
  const username = document.querySelector('input[name=username]');
  const password = document.querySelector('input[name=password]');
  const errorBanner = document.querySelector('#errorBanner');

  function displayError(message) {
    errorBanner.innerHTML = message;
    errorBanner.classList.remove('hidden');
  }

  function submitLogin(e) {
    var loginInfo = {
      username: username.value,
      password: password.value
    };

    var config = {
      method: 'POST',
      credentials: 'include',
      body: JSON.stringify(loginInfo)
    };

    fetch('../LoginServlet', config)
      .then((response) => response.json())
      .then((jsonData) => {
        if(jsonData.success) {
          window.location.href = '..';
        } else {
          displayError(jsonData.message);
        }
      })
      .catch(err => console.log(err.message));
    e.preventDefault();
  }

  form.addEventListener('submit', submitLogin);
})();
