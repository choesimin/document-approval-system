const SUCCESS = 100;
const NO_TOKEN = 200;
const NO_DATA = 300;
const UNSUITABLE_DATA = 310;

const tokenCheck = (code) => {
  if (code == 200) {
    location.href = "/";
  }
}

export {
  SUCCESS,
  NO_TOKEN,
  NO_DATA,
  UNSUITABLE_DATA,
  tokenCheck
}

