export const getRandomHexColor = (max: string): string => {
    if (max.startsWith("#")) {
        max = max.substring(1);
    }
    let randomHex = Math.floor(Math.random() * parseInt(max, 16)).toString(16);
    while (randomHex.length < 6) {
        randomHex = "0" + randomHex;
    }
    return "#" + randomHex;
};

export const addHexColors = (a: string, b: string): string => {
    if (a.startsWith("#")) {
        a = a.substring(1);
    }

    if (b.startsWith("#")) {
        b = b.substring(1);
    }

    let aPlusB = (parseInt(a, 16) + parseInt(b, 16)).toString(16);

    while (aPlusB.length < 6) {
        aPlusB = "0" + aPlusB;
    }

    return "#" + aPlusB;
};

export const subtractHexColors = (a: string, b: string): string => {
    if (a.startsWith("#")) {
        a = a.substring(1);
    }

    if (b.startsWith("#")) {
        b = b.substring(1);
    }

    let aMinusB = (parseInt(a, 16) - parseInt(b, 16)).toString(16);

    while (aMinusB.length < 6) {
        aMinusB = "0" + aMinusB;
    }

    return "#" + aMinusB;
};