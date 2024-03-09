import { useState, useEffect } from "react";

function App() {
  const [data, setData] = useState({});

  useEffect(() => {
    fetch("/api/openmaps")
      .then(response => response.json())
      .then( data => {
          setData(data);
          console.log(data);
        }
      )
  });

  return (
    <pre>
      {JSON.stringify(data, null, 2)}
    </pre>
  );
}

export default App;
