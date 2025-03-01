async function loadChart() {
    const response = await fetch(`/stats/get`);
    const data = await response.json();

    console.log(data);

    const months = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
    const purchases = new Array(12).fill(0);
    const sales = new Array(12).fill(0);

    data.forEach(d => {
        if (d.type === "purchase") {
            purchases[d.month - 1] = d.quantity;
        } else if (d.type === "sale") {
            sales[d.month - 1] = d.quantity;
        }
    });

    console.log(purchases);
    console.log(sales);

    const ctx = document.getElementById("graficoTransacciones").getContext("2d");
    new Chart(ctx, {
        type: "line",
        data: {
            labels: months,
            datasets: [
                { label: "Compras", data: purchases,
                borderColor: "blue",           // Line color
                pointBackgroundColor: "rgba(150, 150, 255, 1)",
                pointHoverRadius: 7,           // Point size on hover
                backgroundColor: "rgba(0, 0, 255, 0.2)", // Area color under the line (transparent)
                fill: true,                     // Enable fill under the line
                borderWidth: 2,                 // Line width
                pointHoverBorderWidth: 2,      // Point border width on hover
                pointRadius: 5,                 // Point size
                tension: 0.1                  // Line curvature
                 },
                { label: "Ventas", data: sales,
                    borderColor: "green",
                    pointBackgroundColor: "rgba(150, 255, 150, 1)",
                    pointHoverRadius: 7,
                    backgroundColor: "rgba(0, 255, 0, 0.2)",
                    fill: true,
                    borderWidth: 2,
                    pointHoverBorderWidth: 2,
                    pointRadius: 5,
                    tension: 0.1                         
                }]
        },
        options: {
            responsive: true,
            scales: { y: { beginAtZero: true } }
        }
    });
}

document.addEventListener('DOMContentLoaded', loadChart);