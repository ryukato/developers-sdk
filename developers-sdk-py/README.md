# Line developers-sdk-py
This is written by Python to support development with Python.

## Start development
### Copy repository
1. clone this repository
  - `git clone https://github.com/ryukato/developers-sdk.git`
2. Change directory to `developers_sdk_py`
  - `cd developers_sdk_py`

### Setup environment
#### Using `venv`
1. Create virtual environment
  - `python3 -m venv venv`
2. Activate the virtual environment
  - `source venv/bin/activate`
3. Run tests to see everything working fine.
  - `python setup.py test`

#### Using pipenv
1. if you don't have `pipenv`, then please install it by run comment.
  - `pip install pipenv`
2. Activate virtual environment.
  - `pipenv shell`
2. Run below command to setup virtual environment and install requirements.
  - `pipenv install`
3. Run tests to see everything working fine.
  - `python -m unittest tests/*.py`

> Note
>
> This project support `venv` and `pipenv`, then we have to update both `setup.py` and `Pipfile` whenever install a new requirement.


## Additional resources
This section has some resources for Python beginner like me. :)
### Learning Python
* This is youtube channel for Python beginner: https://bit.ly/3q14Ddq
### Python3 Official document
* Nothing better than official document: https://docs.python.org/3
### Uplink
* Http Client for python, which is used in this sdk.
* document: https://uplink.readthedocs.io/en/stable/index.html
