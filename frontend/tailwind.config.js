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
      cursor: {
        magic: "url(./src/assets/wand.cur), default",
      },
    },
  },
  plugins: [],
};
