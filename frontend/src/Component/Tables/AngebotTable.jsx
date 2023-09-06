import React, {useState } from 'react';

export function AngebotTable({angebote}){

    const [isChecked, setIsChecked] = useState(false);

    const handleButtonClick = () => {
        setIsChecked(!isChecked)
    }

    return (
        <div> <h3 onClick={handleButtonClick}>Angebote</h3>
        {isChecked &&
            <table>
                <thead>
                    <tr>
                        <th>Produktnummer</th>
                        <th>Filiale</th>
                        <th>Preis</th>
                        <th>Zustand</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>{angebote.prodnr}</td>
                        <td>
                            <table>
                                <thead>
                                    <tr>Filialen Name</tr>
                                </thead>
                                <tbody>
                                    {angebote.fnamen.map((filiale, fIndex)=>
                                        <tr key ={fIndex}>
                                        <td>{filiale}</td>
                                        </tr>  
                                    )}
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table>
                                <thead>
                                    <tr>Preis</tr>
                                </thead>
                                <tbody>
                                    {angebote.preise.map((preis, pIndex)=>
                                        <tr key ={pIndex}>
                                            {(preis === -1) && <td>Momentan kein Angebot vorhanden</td>}
                                            {!(preis === -1) && <td>{preis}</td>}
                                        </tr>  
                                    )}
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table>
                                <thead>
                                    <tr>Zustand</tr>
                                </thead>
                                <tbody>
                                    {angebote.zustände.map((zustand, zIndex)=>
                                        <tr key ={zIndex}>
                                        <td>{zustand}</td>
                                        </tr>  
                                    )}
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </tbody>
            </table>
            }
        </div>
    )
}

export default AngebotTable;