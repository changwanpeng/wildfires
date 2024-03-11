import "bootstrap/dist/css/bootstrap.min.css";
import { useState, useEffect } from "react";
import { Table } from "reactstrap";

function App() {
  const [data, setData] = useState({});
  const [geoDesc, setGeoDesc] = useState(["",""]);
  const [fireCause, setFireCause] = useState(["",""]);
  const [fireStatus, setFireStatus] = useState(["",""]);
  const [pageSize, setPageSize] = useState(100);
  const [page, setPage] = useState(0);
  const totalPages = Math.ceil(data.features && data.features.length / pageSize) || 0;
  const start = page * pageSize;
  const end = page === totalPages - 1 ? data.features.length : (page + 1) * pageSize;
  const features = data.features && data.features.slice(start, end);

  const queryParams = () => {
    let query = [];
    if (geoDesc && geoDesc[0] && geoDesc[1]) {
      let queryString = `GEOGRAPHIC_DESCRIPTION${geoDesc[0]}'${geoDesc[1]}'`;
      query.push(`geoDesc=${encodeURIComponent(queryString)}`);
    };
    if (fireCause && fireCause[0] && fireCause[1]) {
      let queryString = `FIRE_CAUSE${fireCause[0]}'${fireCause[1]}'`;
      query.push(`fireCause=${encodeURIComponent(queryString)}`);
    };
    if (fireStatus && fireStatus[0] && fireStatus[1]) {
      let queryString = `FIRE_STATUS${fireStatus[0]}'${fireStatus[1]}'`;
      query.push(`fireStatus=${encodeURIComponent(queryString)}`);
    };
    return (query?.length) ? '?' + query.join('&'): '';
  };

  useEffect(() => {
    fetch("/api/openmaps" + queryParams())
      .then(response => response.json())
      .then( data => {
          setData(data);
          console.log({pageSize, page, totalPages, start, end});
        }
      )
  }, [geoDesc, fireCause, fireStatus]);

  return (
    <div>
      <h2 className="bold">BC Wildfires</h2>
      <div>Geographic Description:
      <select id="geoDescSelect" value={geoDesc[0]} onChange={e => { setGeoDesc([e.target.value, geoDesc[1]]); setPage(0);}}>
        <option value="">--</option>
        <option value="=">Equal</option>
        <option value="<>">Not Equal</option>
      </select>
      <input id="geoDescInput" type="text" value={geoDesc[1]} onChange={e => { setGeoDesc([geoDesc[0], e.target.value]); setPage(0);}} />
      </div>
      <div>Fire Cause:
      <select id="fireCauseSelect" value={fireCause[0]} onChange={e => { setFireCause([e.target.value, fireCause[1]]); setPage(0);}}>
      <option value="">--</option>
        <option value="=">Equal</option>
        <option value="<>">Not Equal</option>
      </select>
      <input id="fireCauseInput" type="text" value={fireCause[1]} onChange={e => { setFireCause([fireCause[0], e.target.value]); setPage(0);}} />
      </div>
      <div>Fire Status:
      <select id="fireStatusSelect" value={fireStatus[0]} onChange={e => { setFireStatus([e.target.value, fireStatus[1]]); setPage(0);}}>
      <option value="">--</option>
        <option value="=">Equal</option>
        <option value="<>">Not Equal</option>
      </select>
      <input id="fireStatusInput" type="text" value={fireStatus[1]} onChange={e => { setFireStatus([fireStatus[0], e.target.value]); setPage(0);}} />
      </div>
      <div>Page Size:
      <select id="pageSizeSelect" value={pageSize} onChange={e => {setPageSize(e.target.value); setPage(0);}}>
        {
          [100, 200, 300, 400, 500, 1000].map((size, index) => (
            <option key={index} value={size}>{size}</option>
          ))
        }
      </select>
      <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page: </span>
      {
        [...Array(totalPages).keys()].map(pageIndex => (
          (pageIndex === page) ? 
            <button key={pageIndex} onClick={e => setPage(pageIndex)} style={{"backgroundColor": 'gray'}}><b>{pageIndex+1} </b></button> :
            <button key={pageIndex} onClick={e => setPage(pageIndex)}>{pageIndex+1} </button>
        ))
      }
      </div>
      <p><br /><br /></p>
      <Table bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Coordinates</th>
            <th>Fire Number</th>
            <th>Fire Year</th>
            <th>Response Type</th>
            <th>Ignition Date</th>
            <th>Fire Out Date</th>
            <th>Fire Status</th>
            <th>Fire Cause</th>
            <th>Fire Centre</th>
            <th>Zone</th>
            <th>Fire ID</th>
            <th>Fire Type</th>
            <th>Incident Name</th>
            <th>Geographic Description</th>
            <th>Latitude</th>
            <th>Longtitude</th>
            <th>Current Size</th>
            <th>Fire URL</th>
            <th>Feature Code</th>
            <th>Object ID</th>
          </tr>
        </thead>
        <tbody>
          {
            features && features.map((feature, index) => (
              <tr key={index}>
                <td>{feature.id}</td>
                <td>({feature.geometry.coordinates.join(', ')})</td>
                <td>{feature.properties.FIRE_NUMBER}</td>
                <td>{feature.properties.FIRE_YEAR}</td>
                <td>{feature.properties.RESPONSE_TYPE_DESC}</td>
                <td>{feature.properties.IGNITION_DATE}</td>
                <td>{feature.properties.FIRE_OUT_DATE}</td>
                <td>{feature.properties.FIRE_STATUS}</td>
                <td>{feature.properties.FIRE_CAUSE}</td>
                <td>{feature.properties.FIRE_CENTRE}</td>
                <td>{feature.properties.ZONE}</td>
                <td>{feature.properties.FIRE_ID}</td>
                <td>{feature.properties.FIRE_TYPE}</td>
                <td>{feature.properties.INCIDENT_NAME}</td>
                <td>{feature.properties.GEOGRAPHIC_DESCRIPTION}</td>
                <td>{feature.properties.LATITUDE}</td>
                <td>{feature.properties.LONGITUDE}</td>
                <td>{feature.properties.CURRENT_SIZE}</td>
                <td>{feature.properties.FIRE_URL}</td>
                <td>{feature.properties.FEATURE_CODE}</td>
                <td>{feature.properties.OBJECTID}</td>
              </tr>
            ))
          }
        </tbody>
      </Table>
    </div>
  );
}

export default App;
