# Standard library imports
from setuptools import setup, find_packages


def read(filename):
    with open(filename) as stream:
        return stream.read()


install_requires = ["uplink>=0.9.3"]
extras_require = {}
setup_requires = ["pytest-runner"]
tests_require = ["pytest==4.4.1"]

setup(
    name="link_developers_sdk_py",
    author="Yoonyoul Yoo",
    author_email="ryukato79@gmail.com",
    url="https://github.com/ryukato/developers-sdk",
    license="MIT",
    description="link-developers-sdk for python",
    long_description=read("README.md"),
    packages=find_packages(include=["sdk"], exclude=("tests", "tests.*")),
    version="0.0.1",
    install_requires=install_requires,
    setup_requires=setup_requires,
    tests_require=tests_require,
    test_suite="tests",
)
