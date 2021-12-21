import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js'
import { getAuth, signInWithEmailAndPassword, sendPasswordResetEmail, signOut } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js';

const firebaseConfig = {
    apiKey: "AIzaSyDxUQZc18COhEX0v5Uzbxm_26EeSUiormw",
    authDomain: "splat-ce92b.firebaseapp.com",
    projectId: "splat-ce92b",
    storageBucket: "splat-ce92b.appspot.com",
    messagingSenderId: "168823335000",
    appId: "1:168823335000:web:3eea4012a5264f4343822d"
  };
  
const app = initializeApp(firebaseConfig);

window.registerAccount = function registerAccount() {
    let email = document.getElementById("emailTxt").value
    let password = document.getElementById("passwordTxt").value
    let passwordConfirm = document.getElementById("passwordConfirmTxt").value

    if (password != passwordConfirm) {
        alert("Passwords did not match. Please try again.")
    }
    else if (!validateEmail(email)) {
        alert("Please enter a valid email.")
    }
    else if (password.length < 6) {
        alert("Password must be at least 6 characters long.")
    }
    else {
            let requestObject = {
                "email": email,
                "password": password
            }

            fetch('https://splat-ce92b.ue.r.appspot.com/accounts/register', {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(requestObject),
            })
            .then((response) => {
                if (response.ok) {
                    alert("Account succesfully registered.");
                    window.location.href = 'Login.html'
                } 
                else {
                    response.json().then(data => {
                        alert(data.error);
                    })
                    throw new Error('Something went wrong');
                }
            })
            .catch((error) => {
                console.log(error)
            });
        }
}

function validateEmail(email) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

window.login = function login() {
    let email = document.getElementById("emailTxt").value
    let password = document.getElementById("passwordTxt").value

    const auth = getAuth();

    signInWithEmailAndPassword(auth, email, password)
    .then((userCredential) => {
      // Signed in 
      const user = userCredential.user;
      window.location.href = 'index.html'
    })
    .catch((error) => {
      const errorCode = error.code;
      const errorMessage = error.message;

      if (errorCode == 'auth/invalid-email') {
          alert("Invalid email");
      }
      else if (errorCode == 'auth/user-not-found') {
          alert("Email not registered");
      }
      else if (errorCode == 'auth/wrong-password') {
          alert("Incorrect password");
      }
      else if (errorCode == 'auth/too-many-requests') {
          alert("Too many login attempts. Please try again later.")
      }
      else {
          alert(errorCode);
      }
    });
}

export function logout() {
    const auth = getAuth();
    signOut(auth).then(() => {
        alert("Signed out");
    }).catch((error) => {
    // An error happened.
    });
    
    return false
}

window.resetPassword = function resetPassword() {
    let email = document.getElementById("emailTxt").value

    const auth = getAuth();

    sendPasswordResetEmail(auth, email)
    .then(() => {
        alert("Password reset email sent");
        document.getElementById("emailTxt").value = "";
    })
    .catch((error) => {
        const errorCode = error.code;
        const errorMessage = error.message;

        if (errorCode == 'auth/invalid-email') {
            alert("Invalid email")
        }
        else if (errorCode == 'auth/user-not-found') {
            alert("Email not registered");
        }
        else {
            alert(errorCode);
        }
    });
}