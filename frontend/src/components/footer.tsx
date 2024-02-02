import { Link } from "react-router-dom";

export default function Footer() {
  const currentYear: number = new Date().getFullYear();

  return (
    <div className="flex h-10 w-full flex-col items-center justify-end gap-1 bg-gray-50 px-5 text-xs text-gray-400/75 sm:flex-row sm:justify-normal sm:gap-4">
      © {currentYear} HexHex Game. All rights reserved.
      <div className="flex gap-4">
        <Link to="/">Terms & Conditions</Link>
        <Link to="/">Privacy Policy</Link>
      </div>
    </div>
  );
}
