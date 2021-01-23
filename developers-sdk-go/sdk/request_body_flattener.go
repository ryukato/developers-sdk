//
// This module implements flattening request.
//

package sdk

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
            default:
                expr = fmt.Sprint(value)
                is_primitive = true;
        }

        if is_primitive {
            flatPair[key] = expr;
        }
    }
}

func Flatten(body map[string]interface{}) string {
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

    return ret;
}



