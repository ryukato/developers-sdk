//
// This module implements flattening request.
//

//package RequestBodyFlattener
package main

import (
    "fmt"
    "sort"
    "strings"
)

func _createFlatPair(flatPair map[string]string, body map[string]interface{}) {
    for key, value := range(body) {
        is_primitive := false;
        expr := "";

        switch value.(type) {
            case []interface{}:
                allSubKeys := make(map[string]bool, 0)

                for _, subv:= range value.([]interface{}) {
                    submap := subv.(map[string]interface{})

                    for k, _ := range(submap) {
                        allSubKeys[k] = true;
                    }
                }

                for _, subv:= range value.([]interface{}) {
                    submap := subv.(map[string]interface{})

                    for subKey, _ := range(allSubKeys) {
                        flatKey := fmt.Sprintf("%s.%s", key, subKey)

                        flatRawValue := "";

                        if x, found := submap[subKey]; found {
                            flatRawValue = x.(string)
                        }

                        if prevFlatValue, found := flatPair[flatKey]; found {
                            flatPair[flatKey] = fmt.Sprintf("%s,%s", prevFlatValue, flatRawValue)
                        } else {
                            flatPair[flatKey] = flatRawValue
                        }
                    }
                }

            // handle primitive types
            case int:
                expr = fmt.Sprintf("%d", value)
                is_primitive = true;

            case float32:
                expr = fmt.Sprintf("%f", value)
                is_primitive = true;

            case float64:
                expr = fmt.Sprintf("%lf", value)
                is_primitive = true;

            case string:
                expr = value.(string)
                is_primitive = true;
        }

        if is_primitive {
            flatPair[key] = expr;
        }
    }

    fmt.Println(flatPair)
}

func flatten(body map[string]interface{}) string {
    flatPair := make(map[string]string) // we're going to convert objBody to flatPair

    _createFlatPair(flatPair, body)

    keys := make([]string, 0, len(flatPair))
    flattenBody := make([]string, 0, len(flatPair))

    for k := range flatPair {
        keys = append(keys, k)
    }

    sort.Strings(keys)

    for _, k := range keys {
        flattenBody = append(flattenBody, fmt.Sprintf("%s=%s", k, flatPair[k]))
    }

    ret := strings.Join(flattenBody, "&")
    fmt.Println(ret)

    return ret;
}

func main() {
    query := map[string]interface{}{
       "ownerAddress": "tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq",
       "ownerSecret": "uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=",
       "toAddress": "tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp",
       "mintList": []interface{}{
           map[string]interface{}{
               "tokenType": "10000001",
               "name": "NewNFT",
           },
           map[string]interface{}{
               "tokenType": "10000003",
               "name": "NewNFT2",
               "meta": "New nft 2 meta information",
           },
       },
    };

    flatone := flatten(query)
    expected := "mintList.meta=,New nft 2 meta information&mintList.name=NewNFT,NewNFT2&mintList.tokenType=10000001,10000003&ownerAddress=tlink1fr9mpexk5yq3hu6jc0npajfsa0x7tl427fuveq&ownerSecret=uhbdnNvIqQFnnIFDDG8EuVxtqkwsLtDR/owKInQIYmo=&toAddress=tlink18zxqds28mmg8mwduk32csx5xt6urw93ycf8jwp"
    fmt.Println(expected)

    fmt.Println(flatone == expected)
}



