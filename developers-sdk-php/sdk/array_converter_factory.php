<?php

namespace LineDevelopersSdk;

require_once 'vendor/autoload.php';

use GuzzleHttp\Psr7;
use Psr\Http\Message\StreamInterface;
use Tebru\PhpType\TypeToken;
use Tebru\Retrofit\RequestBodyConverter;
use Tebru\Retrofit\ResponseBodyConverter;
use Tebru\Retrofit\ConverterFactory;
use Tebru\Retrofit\StringConverter;

class ArrayConverterFactory implements ConverterFactory {
    public function __construct() {
    }

    public function responseBodyConverter(TypeToken $type): ?ResponseBodyConverter
    {
        return new ArrayResponseBodyConverter($type);
    }

    public function requestBodyConverter(TypeToken $type): ?RequestBodyConverter
    {
        return new ArrayRequestBodyConverter($type);
    }

    public function stringConverter(TypeToken $type): ?StringConverter
    {
        return null;
    }
}

class ArrayResponseBodyConverter implements ResponseBodyConverter {
    private $type;

    public function __construct(TypeToken $type)
    {
        $this->type = $type;
    }

    public function convert($value): StreamInterface
    {
        if ($this->type->isA(StreamInterface::class)) {
            return $value;
        }

        /** @noinspection ExceptionsAnnotatingAndHandlingInspection */
        return Psr7\stream_for(json_encode($value));
    }
}

class ArrayRequestBodyConverter implements RequestBodyConverter {
    private $type;

    public function __construct(TypeToken $type)
    {
        $this->type = $type;
    }

    public function convert($value): StreamInterface
    {
        if ($this->type->isA(StreamInterface::class)) {
            return $value;
        }

        /** @noinspection ExceptionsAnnotatingAndHandlingInspection */
        return Psr7\stream_for(json_encode($value));
    }
}

?>
