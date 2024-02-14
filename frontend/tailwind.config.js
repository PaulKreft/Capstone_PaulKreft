import defaultTheme from "tailwindcss/defaultTheme";

/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    screens: {
      xs: "380px",
      ...defaultTheme.screens,
    },
    extend: {
      animation: {
        'spin-slow': 'spin 2s linear infinite',
      },
      cursor: {
        magic: "url(wand.cur), default",
      },
      colors: {
        green: "#73BA9B",
        blue: "#6D98BA",
        red: "#BA2D0B",
      },
    },
  },
  plugins: [],
};
