# conversor-moneda-tipo-cambio
API REST para aplicar un tipo de cambio a un monto

    1. Registrar Moneda - Metodo POST: http://localhost:8080/conversor/moneda
   Request:
   {
    "nombreMoneda": "EURO",
    "tipoCambio": 3.96
   }
   
   Response:
   {
    "idMoneda": 4,
    "nombreMoneda": "EURO",
    "tipoCambio": 3.96
   }

    2. Actualizar Moneda - Metodo PUT: http://localhost:8080/conversor/moneda/{nombreMoneda}
   Request:
   {
    "nombreMoneda": "DOLAR",
    "tipoCambio": 3.75
   }
   
   Response:
   {
    "idMoneda": 2,
    "nombreMoneda": "DOLAR",
    "tipoCambio": 3.75
   }
   
    3. Calcular Tipo de Cambio - Metodo POST: http://localhost:8080/conversor/moneda
   Request:
   {
    "monto": 253.408233,
    "monedaOrigen": "Soles",
    "monedaDestino": "euro"
   }
   
   Response:
   {
    "montoOriginal": 253.41,
    "montoConvertido": 1003.50,
    "monedaOrigen": "SOLES",
    "monedaDestino": "EURO",
    "tipoCambio": 3.96
   }

    4. Listar Monedas - Metodo GET: http://localhost:8080/conversor/monedas
   Response:
   [
    {
        "idMoneda": 1,
        "nombreMoneda": "EURO",
        "tipoCambio": 3.96
    },
    {
        "idMoneda": 2,
        "nombreMoneda": "DOLAR",
        "tipoCambio": 3.65
    },
    {
        "idMoneda": 3,
        "nombreMoneda": "PESOS",
        "tipoCambio": 2.43
    }
]
