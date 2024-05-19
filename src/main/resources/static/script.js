// Mapping von Klassifikationsnummern zu Namen
const classMapping = {
    0: 'T-Shirt/Top',
    1: 'Trouser',
    2: 'Pullover',
    3: 'Dress',
    4: 'Coat',
    5: 'Sandal',
    6: 'Shirt',
    7: 'Sneaker',
    8: 'Bag',
    9: 'Ankle boot'
};

function checkFiles(files) {
    console.log(files);

    if (files.length !== 1) {
        alert("Bitte genau eine Datei hochladen.");
        return;
    }

    const fileSize = files[0].size / 1024 / 1024; // in MiB
    if (fileSize > 10) {
        alert("Datei zu groß (max. 10 MB).");
        return;
    }

    const preview = document.getElementById('preview');
    const answerPart = document.getElementById('answerPart');
    const answerTable = document.getElementById('answer');

    preview.src = URL.createObjectURL(files[0]);
    answerPart.style.visibility = "visible";

    const formData = new FormData();
    formData.append("image", files[0]);

    fetch('/analyze', {
        method: 'POST',
        body: formData
    }).then(response => response.json())
    .then(data => {
        answerTable.innerHTML = ''; // Leere vorherige Ergebnisse
        data.forEach(prediction => {
            const className = prediction.className; // Annahme, dass die Klassifikationsnummer als 'class' zurückgegeben wird
            const classCategory = classMapping[className]; // Den Namen der Kategorie aus dem Mapping holen
            const probability = prediction.probability.toFixed(4); // Formatieren der Wahrscheinlichkeit als Dezimal
            const probabilityPercent = (prediction.probability * 100).toFixed(1) + '%'; // Umwandlung in Prozent
            let row = `<tr>
                <td>${className} - ${classCategory}</td>
                <td>${probability} (${probabilityPercent})</td>
            </tr>`;
            answerTable.innerHTML += row;
        });
    })
    .catch(error => console.error("Fehler beim Fetch: ", error));
}
