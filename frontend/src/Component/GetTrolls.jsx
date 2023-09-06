import React, {useState } from 'react';
import axios from 'axios';
import TrollSearch from './TrollSearch'

export function GetTrolls(){

    const [trollList, setTrollList] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);

    const handleSearch = (limit) => {
        setHasSearched(true);
        axios
          .get(`http://localhost:8080/getTrolls?limit=${limit}`)
          .then((res) => setTrollList(res.data))
          .catch((err) => console.log(err));
      };
    


    return(
        <div>
        <TrollSearch onSearch={handleSearch}/>
        {hasSearched && trollList && trollList.length > 0 && (
            <div>
                <table >
                    <thead>
                        <tr>
                            <th>Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {trollList.map( (troll, index) => {
                            if (index % 10 === 0) {
                                return (
                                  <tr key={index}>
                                    <td>{trollList[index].name}</td>
                                    <td>{trollList[index + 1]?.name}</td>
                                    <td>{trollList[index + 2]?.name}</td>
                                    <td>{trollList[index + 3]?.name}</td>
                                    <td>{trollList[index + 4]?.name}</td>
                                    <td>{trollList[index + 5]?.name}</td>
                                    <td>{trollList[index + 6]?.name}</td>
                                    <td>{trollList[index + 7]?.name}</td>
                                    <td>{trollList[index + 8]?.name}</td>
                                    <td>{trollList[index + 9]?.name}</td>
                                  </tr>
                                );
                              }
                              // If there are less than 3 trolls in the current row, adjust the number of columns
                              else {
                                return null; // Skip this row
                              }
                            })}
                    </tbody>
                </table>
            </div>
        )}
        {hasSearched && trollList.length < 1 && (
            <h4>Keine Trolle gefunden</h4>
        )}
        </div>
    )
}

export default GetTrolls;