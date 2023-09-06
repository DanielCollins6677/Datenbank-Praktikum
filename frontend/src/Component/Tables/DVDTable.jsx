import React, {useState } from 'react';

export function DVDTable({ productList }){

    const [isChecked, setIsChecked] = useState(false);

    const filteredList = productList.filter((product) => {
        // Replace the condition with one that matches the specific type of product you want for Table C
        return product.laufzeit;
      });

      const handleButtonClick = () => {
        setIsChecked(!isChecked)
    }

    if(filteredList.length > 0){
        return (
            <h3> <div onClick={handleButtonClick}>DVDs</div>
            {isChecked &&
                <table className="produktTable">
                    <thead>
                        <tr>
                            <th>Produktnummer</th>
                            <th>Titel</th>
                            <th>Rating</th>
                            <th>Rang</th>
                            <th>Bild</th>
                            <th>Laufzeit</th>
                            <th>Regionscode</th>
                            <th>Beteiligte</th>
                            <th>Formate</th>
                            {/* Add specific attributes for Table C */} 
                        </tr>
                    </thead>
                    <tbody>
                    {filteredList.map( (product, index)=> (
                        <tr key={index}>
                            <td>{product.prodnr}</td>
                            <td>{product.titel}</td>
                            <td>{product.rating}</td>
                            <td>{product.rang}</td>
                            <td>{product.bild}</td>
                            <td>{product.laufzeit}</td>
                            <td>{product.regioncode}</td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Beteiligten Titel</th>
                                            <th>Beteiligten Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {product.beteiligte.map( (beteiligte, beteiligtenIndex) => (
                                            <tr key={beteiligtenIndex}>
                                                <td>{beteiligte.titel}</td>
                                                <td>{beteiligte.name}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Formats Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {product.formate.map( (format, formatsIndex) => (
                                            <tr key={formatsIndex}>
                                                <td>{format}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </td>
                            {/* Add specific attributes for Table C */}
                        </tr>
                    ))}
                    </tbody>
                </table>
                }
            </h3>
        )
    }
}

export default DVDTable;