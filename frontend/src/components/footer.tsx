import { Link } from "react-router-dom";

export default function Footer() {
  const currentYear: number = new Date().getFullYear();

  return (
    <div className="flex h-20 w-full flex-col items-center justify-center gap-4 bg-gray-50 px-5 text-xs text-gray-400/75 sm:h-10 sm:flex-row sm:justify-normal">
      © {currentYear} HexHex Game. All rights reserved.
      <div className="flex gap-4">
        <Link to="/">Terms & Conditions</Link>
        <Link to="/">Privacy Policy</Link>
      </div>
    </div>
  );
}
