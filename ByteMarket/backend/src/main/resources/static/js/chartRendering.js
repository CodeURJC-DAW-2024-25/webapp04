async function cargarGrafico() {
    const response = await fetch(`/stats/get`);
    const datos = await response.json();

    const meses = ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"];
    const compras = new Array(12).fill(0);
    const ventas = new Array(12).fill(0);

    datos.forEach(d => {
        if (d.tipo === "purchase") {
            compras[d.mes - 1] = d.cantidad;
        } else if (d.tipo === "sale") {
            ventas[d.mes - 1] = d.cantidad;
        }
    });

    const ctx = document.getElementById("graficoTransacciones").getContext("2d");
    new Chart(ctx, {
        type: "line",
        data: {
            labels: meses,
            datasets: [
                { label: "Compras", data: compras,
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
                { label: "Ventas", data: ventas,
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

document.addEventListener('DOMContentLoaded', cargarGrafico);