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
    const quantityElement = document.getElementById(quantityId);
    const quantity = quantityElement.value;
    if (activeCart === false) {
        await fetch(`${BASE_URL}/cart/newCart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(async (response) => await response.json())
            .then((data) => {
                console.log("Success:", data);
                cartId = data.cart_id;
                alert("new cart created");
                activeCart = true;
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("Error creating cart");
            });
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
const barcodeQuantityMap = {};
async function addToCartWBarcode(barcode) {
    if (!barcode.trim()) return;
    // const quantityElement = document.getElementById(quantityId);
    // const quantity = quantityElement.value;

    if (barcodeQuantityMap[barcode]) {
        barcodeQuantityMap[barcode] += 1;
    } else {
        barcodeQuantityMap[barcode] = 1;
    }

    if (activeCart === false) {
        await fetch(`${BASE_URL}/cart/newCart`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then(async (response) => await response.json())
            .then((data) => {
                console.log("Success:", data);
                cartId = data.cart_id;
                alert("new cart created");
                activeCart = true;
            })
            .catch((error) => {
                console.error("Error:", error);
                alert("Error creating cart");
            });
    }

    //, cartId) {
    //console.log(product_id);
    // console.log(quantity);
    const dataOut = {
        barcode: barcode,
        quantity: barcodeQuantityMap[barcode],
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
        const data = await response.json();
        console.log(data);

        const container = document.getElementById("cart-items-container");
        container.innerHTML = "";

        for (const item of data) {
            let productId = item.product_id;
            let productName = "";
            let productPrice = "";
            try {
                const productResponse = await fetch(
                    `${BASE_URL}/products/${productId}`,
                    {
                        method: "Get",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                );

                if (!response.ok) {
                    throw new Error("could not fetch");
                }
                const productData = await productResponse.json();
                productName = productData.name;
                productPrice = productData.price;
                console.log(productName);
                console.log(productPrice);
            } catch (error) {
                console.error(error);
            }

            const rowDiv = document.createElement("div");
            rowDiv.classList.add("row", "mb-2");

            rowDiv.innerHTML = `
            <div class="col-4">
                <div class="row"> ${productName}</div>
                
            </div>
            <div class="col-4">
                <div class="row"> Price: $${productPrice} </div>
            
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
                                <button class="btn btn-success mt-2 w-50" onclick="addToCart('${item.product_id}', 'quantityInput')">
                            </li>
                        </ul>
                    </div>
                </div>

            </div>
            
            `;

            container.appendChild(rowDiv);
        }
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

            if (!response.ok) {
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
    try {
        const saleResponse = await fetch(
            `${BASE_URL}/sale/completeSale/${cartId}/${cash.value}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "userId":2
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

        if(loginData == true){
            authenticated = true;
            sessionStorage.setItem("isAuthenticated", "true")
            window.location.href = "/products/";
        }else{
            authenticated = false;
            window.location.reload();
        }
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

window.onload = function() {
    document.getElementById("barcodeSearch").focus();
};


