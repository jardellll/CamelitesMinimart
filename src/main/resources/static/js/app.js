let cartId = 0;
let activeCart = false;
let total = 0.00;
let cash = document.getElementById("cash")
let username = document.getElementById("username")
let password = document.getElementById("password")
let change = 0.00
let authenticated = false;
// document.getElementById("newCart").addEventListener("click", function () {
//   fetch('${BASE_URL}/cart/newCart", {
//     method: "POST",
//     headers: {
//       "Content-Type": "application/json",
//     },
//   })
//     .then((response) => response.json())
//     .then((data) => {
//       console.log("Success:", data);
//       cartId = data.cart_id;
//       alert(data);
//     })
//     .catch((error) => {
//       console.error("Error:", error);
//       alert("Error creating cart");
//     });
// });
const BASE_URL = window.location.origin;
async function addToCart(product_id, quantityId) {
    let isCreatingCart = false;
    const quantityElement = document.getElementById(quantityId);
    const quantity = quantityElement.value;
    if (!activeCart && !isCreatingCart) {
        isCreatingCart = true;
        try {
            const response = await fetch(`${BASE_URL}/cart/newCart`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });

            if (!response.ok) {
                throw new Error("Error creating cart");
            }

            const data = await response.json();
            console.log("Success:", data);
            cartId = data.cart_id;
            activeCart = true;
        } catch (error) {
            console.error("Error:", error);
            alert("Error creating cart");
        } finally {
            isCreatingCart = false;
        }
    }

    //, cartId) {
    console.log(product_id);
    console.log(quantity);
    const dataOut = {
        product_id: product_id,
        quantity: quantity,
        cart_id: cartId,
    };
    try {
        console.log(product_id);
        console.log(quantity);
        console.log(cartId);
        const response = await fetch(`${BASE_URL}/cartItems/addItem`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(dataOut),
        });

        if (!response.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", response);
        const data = await response.json();

        getCartItems();
    } catch (error) {
        console.error(error);
    }
}
//const barcodeQuantityMap = {};
async function addToCartWBarcode(barcode) {
    let isCreatingCart = false;
    if (!barcode.trim()) return;
    // const quantityElement = document.getElementById(quantityId);
    // const quantity = quantityElement.value;

    // if (barcodeQuantityMap[barcode]) {
    //     barcodeQuantityMap[barcode] += 1;
    // } else {
    //     barcodeQuantityMap[barcode] = 1;
    // }

    if (!activeCart && !isCreatingCart) {
        isCreatingCart = true;
        try {
            const response = await fetch(`${BASE_URL}/cart/newCart`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
            });

            if (!response.ok) {
                throw new Error("Error creating cart");
            }

            const data = await response.json();
            console.log("Success:", data);
            cartId = data.cart_id;
            activeCart = true;
        } catch (error) {
            console.error("Error:", error);
            alert("Error creating cart");
        } finally {
            isCreatingCart = false;
        }
    }

    //, cartId) {
    //console.log(product_id);
    // console.log(quantity);
    const dataOut = {
        barcode: barcode,
        quantity: 1,
        cart_id: cartId,
    };
    try {
        console.log(barcode);
        //console.log(quantity);
        console.log(cartId);
        const response = await fetch(`${BASE_URL}/cartItems/addItem/barcode`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(dataOut),
        });

        if (!response.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", response);
        const data = await response.json();
        document.getElementById("barcodeSearch").value = "";
        
        document.getElementById("barcodeSearch").focus();

        getCartItems();
    } catch (error) {
        console.error(error);
    }
}
async function removeFromCart(product_id, cartId) {
    //const quantityElement = document.getElementById(quantityId);
    const dataOut = {
        cart_id: cartId,
        product_id: product_id,
    };
    try {
        console.log(product_id);
        console.log(cartId);
        const response = await fetch(`${BASE_URL}/cartItems/removeItem`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(dataOut),
        });

        if (!response.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", response);
        // const data = await response.json();
        

        getCartItems();
    } catch (error) {
        console.error(error);
    }
}

async function getCartItems() {
    let data;
    try {
        const response = await fetch(
            `${BASE_URL}/cartItems/listCartItems/` + cartId,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );

        if (!response.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", response);
        data = await response.json();
        console.log(data);

    } catch (error) {
        console.error(error);
    }

    const container = document.getElementById("cart-items-container");
    container.innerHTML = "";

    const productPromises = data.map(async (item) => {
        try {
            const productResponse = await fetch(`${BASE_URL}/products/${item.product_id}`);
            if (!productResponse.ok) throw new Error("Could not fetch product data");

            const productData = await productResponse.json();
            return { ...item, productName: productData.name, productPrice: productData.price };
        } catch (error) {
            console.error(error);
            return { ...item, productName: "Unknown", productPrice: "N/A" };
        }
    });
    
    const products = await Promise.all(productPromises);
    products.forEach(item => {
        const rowDiv = document.createElement("div");
        rowDiv.classList.add("row", "mb-2");
        //rowDiv.innerHTML = `<div class="col-4">${item.productName}</div>`;
        rowDiv.innerHTML = `
        <div class="col-4">
            <div class="row"> ${item.productName}</div>
            
        </div>
        <div class="col-4">
            <div class="row"> Price: $${item.productPrice} </div>
        
            <div class="row"> 
                <button class="btn btn-danger btn-sm" onclick="removeFromCart('${item.product_id}', '${cartId}')">Remove</button>
            </div>
        
        </div>
        <div class="col-4">
        
            <div class="row">Quantity: ${item.quantity}</div>

            <div class="row">
                <div class="btn-group">
                    <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                        Change Quantity
                    </button>
                    <ul class="dropdown-menu p-3" style="min-width: 200px;">
                        <li>
                            <input type="number" id="quantityInput" class="form-control" min="1" placeholder="Enter quantity" onkeydown="event.stopPropagation();">
                        </li>
                        <li>
                            <button class="btn btn-success mt-2 w-50" onclick="addToCart('${item.product_id}', 'quantityInput')"></button>
                        </li>
                    </ul>
                </div>
            </div>

        </div>
        
        `;

        container.appendChild(rowDiv);
    });
        // const rowDiv = document.createElement("div");
        // rowDiv.classList.add("row", "mb-2");

        
    
    try {
        const checkoutResponse = await fetch(
            `${BASE_URL}/cartItems/checkout/${cartId}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );

        if (!checkoutResponse.ok) {
            throw new Error("could not fetch");
        }
        const checkoutData = await checkoutResponse.json();
        console.log(checkoutData);
        total = checkoutData;
        console.log(total);

        totalValueElement = document.getElementById("total-value");
        totalValueElement2 = document.getElementById("total-value-modal");
        totalValueElement.textContent = `$${total}`;
        totalValueElement2.textContent = `$${total}`;

        
    } catch (error) {
        console.error(error);
    }
}


// document.getElementById("complete-sale-btn").addEventListener("click", async () => {
//     await completeSale(); // Call your function
//     const changeModal = new bootstrap.Modal(document.getElementById("changeScreen"));
//     changeModal.show(); // Open the next modal programmatically
// });


async function completeSale(){
    const userIdForSale = sessionStorage.getItem("userId") || "2";
    try {
        const saleResponse = await fetch(
            `${BASE_URL}/sale/completeSale/${cartId}/${cash.value}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "userId":userIdForSale
                },
            }
        );

        if (!saleResponse.ok) {
            throw new Error("could not fetch");
        }
        const saleData = await saleResponse.json();
        console.log(saleData);
        change = saleData;
        console.log(change);

        totalChangeElement = document.getElementById("change-value");
        totalChangeElement.textContent = `$${change}`;

    } catch (error) {
        console.error(error);
    }
}

async function login(event){

    event.preventDefault();
    const loginDetails = {
        username: username.value,
        password: password.value,
    };
    console.log(JSON.stringify(loginDetails));
    try {
        const loginResponse = await fetch(`${BASE_URL}/user/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(loginDetails),
        });

        if (!loginResponse.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", loginResponse);
        const loginData = await loginResponse.json();

        if(loginData.authenticated === true){
            authenticated = true;
            sessionStorage.setItem("isAuthenticated", loginData.authenticated)
            sessionStorage.setItem("userId", loginData.id);
            sessionStorage.setItem("accessLevel", loginData.accessLevel);

            const accountLink = document.getElementById("account-link");
            if (accountLink) {
                accountLink.href = `/user/account/${loginData.id}`;
            }

            updateAccountLink();

            window.location.href = "/products/";
        }else{
            authenticated = false;
            window.location.reload();
        }
    } catch (error) {
        console.error(error);
    }
}

async function createNewUser(formData){

    const username = formData.get('username'); // Get value of username field
    const password = formData.get('password'); // Get value of password field
    const fName = formData.get('fName'); // Get value of first name field
    const lName = formData.get('lName'); // Get value of last name field
    const accessLevel = formData.get('accessLevel'); 

    console.log("Username:", username);
    console.log("Password:", password);
    console.log("First Name:", fName);
    console.log("Last Name:", lName);
    console.log("Access Level:", accessLevel);

    const newUserDetails = {
        username: formData.get('username'),
        password: formData.get('password'),
        fName: formData.get('fName'),
        lName: formData.get('lName'),
        accessLevel: formData.get('accessLevel')
    };
    console.log(JSON.stringify(newUserDetails));

    try {
        const newUserResponse = await fetch(`${BASE_URL}/user/addNewUser`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newUserDetails),
        });

        if (!newUserResponse.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", newUserResponse);
        const newUserData = await newUserResponse.json();
        window.location.reload();
    } catch (error) {
        console.error(error);
    }
}


async function newProduct() {
    let name = document.getElementById("name").value;
    let price = document.getElementById("price").value;
    let quantity = document.getElementById("quantity").value;
    let details = document.getElementById("details").value;
    let barcode = document.getElementById("barcode").value;

    console.log(name);
    console.log(price);
    console.log(quantity);
    console.log(details);
    console.log(barcode);

    const newProductRequest = {
        name: name,
        price: price,
        details: details,
        barcode: barcode,
        quantity: quantity
    };

    try {
        const newProductResponse = await fetch(`${BASE_URL}/products/newProduct`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newProductRequest),
        });

        if (!newProductResponse.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", newProductResponse);
        window.location.reload();
        // const Data = await newProductResponse.json();

        // if(loginData == true){
        //     authenticated = true;
        //     sessionStorage.setItem("isAuthenticated", "true")
        //     window.location.href = "/products/";
        // }else{
        //     authenticated = false;
        //     window.location.reload();
        // }
    } catch (error) {
        console.error(error);
    }


}

async function addStock(product_id, stockQuantity) {
    

    let quantity = document.getElementById(stockQuantity).value;
    console.log(quantity);
    console.log(product_id);

    try {
        const addStockResponse = await fetch(`${BASE_URL}/stock/addStock/${product_id}/${quantity}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!addStockResponse.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", addStockResponse);
        window.location.reload();
        // const Data = await newProductResponse.json();

        // if(loginData == true){
        //     authenticated = true;
        //     sessionStorage.setItem("isAuthenticated", "true")
        //     window.location.href = "/products/";
        // }else{
        //     authenticated = false;
        //     window.location.reload();
        // }
    } catch (error) {
        console.error(error);
    }


}

async function generateDailyReport() {

    let reportDate = document.getElementById("reportDate").value;
    const userIdForReport = sessionStorage.getItem("userId") || "2";
    console.log(reportDate);
    

    try {
        const dailyReportResponse = await fetch(`${BASE_URL}/sale/generate/dayly/report/${userIdForReport}/${reportDate}`, {
            method: "GET", 
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!dailyReportResponse.ok) {
            throw new Error("could not fetch");
        }
        console.log("Success:", dailyReportResponse);
        const blob = await dailyReportResponse.blob();
        try{
            if (blob.size === 0) {
                alert("The report is empty or failed to generate.");
                return;
            }
            
            let url = window.URL.createObjectURL(blob);
            let a = document.createElement("a");
            a.href = url;
            a.download = `DailySalesReport_${reportDate}.pdf`;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
        }
        catch (error) {
            console.error("Error generating report:", error);
            alert("Failed to generate report. Please try again.");
        }

    } catch (error) {
        console.error(error);
    }


}

document.addEventListener("DOMContentLoaded", ()=>{
        const isAuthenticated = sessionStorage.getItem("isAuthenticated");
    
        if (isAuthenticated !== "true" && !window.location.pathname.startsWith("/user/")) {
            window.location.href = "/user/";
        }
});

document.addEventListener("DOMContentLoaded", () => {
    const changeScreenModal = document.getElementById("changeScreen");

    if (changeScreenModal) {
        // Listen for when the modal is shown
        changeScreenModal.addEventListener("shown.bs.modal", () => {
            console.log("Modal is now visible. Adding event listener for hidden event.");
            
            // Add the event listener when the modal is shown
            changeScreenModal.addEventListener("hidden.bs.modal", () => {
                console.log("Modal hidden, reloading page...");
                location.reload();
            }, { once: true }); // `{ once: true }` ensures this runs only once per modal display
        });
    }
});

function filterProducts() {
    let input = document.getElementById('productSearch').value.toLowerCase();
    let items = document.getElementById('productList').getElementsByTagName('li');

    for (let i = 0; i < items.length; i++) {
        let productName = items[i].getElementsByClassName('product-name')[0].innerText.toLowerCase();
        if (productName.includes(input)) {
            items[i].style.display = "";
        } else {
            items[i].style.display = "none";
        }
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const barcodeInput = document.getElementById("barcodeSearch");
    if (barcodeInput) {
        barcodeInput.focus(); // Only focus if the element exists
    }
});

function updateAccountLink() {
    const userId = sessionStorage.getItem("userId"); // Retrieve userId from sessionStorage
    const accountLink = document.getElementById("account-link"); // Select the account link

    if (userId && accountLink) {
        accountLink.href = `/user/account/${userId}`; // Set the correct URL
    } else {
        console.warn("User ID not found in sessionStorage");
    }
}

document.addEventListener("DOMContentLoaded", function () {
    updateAccountLink();
});

document.addEventListener("DOMContentLoaded", function () {
    const createUserForm = document.getElementById("createUserForm");
    if (createUserForm) {
        createUserForm.addEventListener("submit", function (event) {
            event.preventDefault(); // Prevent default form submission (GET request)

            const formData = new FormData(createUserForm); // Capture form data
            createNewUser(formData); // Call your function
        });
    }
});

document.addEventListener("DOMContentLoaded", function () {
    if (document.getElementById("logout-btn")) {
        document.getElementById("logout-btn").addEventListener("click", function () {
            sessionStorage.clear(); // Clear session storage
            window.location.href = "/user/"; 
        });             
    }
});
document.addEventListener("DOMContentLoaded", function () {
    let generateReportBtn = document.getElementById("reportGenerateBtn");
    if (generateReportBtn) {
        document.getElementById("reportGenerateBtn").addEventListener("click", generateDailyReport);
    }
});
document.addEventListener("DOMContentLoaded", function () {
    const accessLevel = sessionStorage.getItem("accessLevel")?.toLowerCase();;
    let newUserForm = document.getElementById("createUserFormRow");
    
    if (newUserForm){
        if (accessLevel !== "manager") { 
            newUserForm.style.display = "none";
        }
    }
});
// function handleSubmit(event) {
//     event.preventDefault(); // Prevent form from submitting in the usual way

//     // Retrieve form data
//     const formData = new FormData(event.target); // Get all form fields
//     const formValues = Object.fromEntries(formData.entries()); // Convert FormData to an object

//     // Send the form data to a JavaScript function or handle it however you like
//     createNewUser(formValues);
// }




