import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js'
import { getAuth } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js'
import { logout } from './accounts.js'
 
const firebaseConfig = {
    apiKey: "AIzaSyDxUQZc18COhEX0v5Uzbxm_26EeSUiormw",
    authDomain: "splat-ce92b.firebaseapp.com",
    projectId: "splat-ce92b",
    storageBucket: "splat-ce92b.appspot.com",
    messagingSenderId: "168823335000",
    appId: "1:168823335000:web:3eea4012a5264f4343822d"
  };
  
const app = initializeApp(firebaseConfig);

const auth = getAuth();

window.logout = logout;
auth.onIdTokenChanged(function(user) {

    if (user) {
        window.location.href = 'index.html';
    }
    else {
        document.getElementsByTagName('html')[0].style.visibility = "visible";
    }
  });



