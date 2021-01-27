
package sdk

import (
    "fmt"
    "strings"
    "crypto/hmac"
    "crypto/sha512"
    "encoding/base64"
)

func __createSignTarget(method string, path string, timestamp int, nonce string, parameters map[string]interface{}) string {
    s := fmt.Sprint(timestamp)
    signTarget := fmt.Sprintf("%s%s%s%s", nonce, s, method, path)

    if len(parameters) > 0 {
        signTarget = fmt.Sprintf("%s?", signTarget)
    }

    return signTarget;
}

func Generate(secret string,
              method string,
              path string,
              timestamp int,
              nonce string,
              query_params map[string]interface{},
              body map[string]interface{}) string {
    //  
    //    Generate signature with given arguments.
    //
    //    Args:
    //        -secret- api-secret
    //        -method- http method
    //        -path- api path
    //        -timestamp- Unix timestamp value
    //        -nonce- random stirng with 8 length
    //        -query_params- query paraemeters
    //        -body- request body
    //
    //    Returns:
    //        -signauture- generated signature
    //        
    //

    all_parameters := make(map[string]interface{})

    for k, v := range query_params {
        all_parameters[k] = v
    }

    for k, v := range body {
        all_parameters[k] = v
    }

    signTarget := __createSignTarget(strings.ToUpper(method), path, timestamp, nonce, all_parameters);

    if len(all_parameters) > 0 {
        signTarget = fmt.Sprintf("%s%s", signTarget, Flatten(all_parameters))
    }

    raw_hmac := hmac.New(sha512.New, []byte(secret))
    raw_hmac.Write([]byte(signTarget))

    result := base64.StdEncoding.EncodeToString(raw_hmac.Sum(nil))

    return result
}
