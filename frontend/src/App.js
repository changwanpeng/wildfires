import { useState, useEffect } from "react";

function App() {
  const [data, setData] = useState({});
  useEffect(() => {
    fetch("https://openmaps.gov.bc.ca/geo/pub/ows?service=WFS&version=2.0.0&request=GetFeature&typeName=pub:WHSE_LAND_AND_NATURAL_RESOURCE.PROT_CURRENT_FIRE_PNTS_SP&outputFormat=application%2Fjson")
      .then(response => response.json())
      .then(data => setData(data))
  });

  return (
    <pre>
      {JSON.stringify(data, null, 2)}
    </pre>
  );
}

export default App;
