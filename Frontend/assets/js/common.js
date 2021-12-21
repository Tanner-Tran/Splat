window.searchRest = function searchRest() {
    let restName = document.getElementById("searchtext").value;

  fetch('https://splat-ce92b.ue.r.appspot.com/restaurants?restName=' + restName, {
    method: 'GET', 
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
    })
    .then((response) => {
        if (response.ok) {
            localStorage.setItem("viewRestaurant", restName);
            window.location.href = "Restaurant.html"
        } 
        else {
            alert(restName + " has no reviews yet. Be the first!")
        }
    })
    .catch((error) => {
        console.log(error)
    });
}