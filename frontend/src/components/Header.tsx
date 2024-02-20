"use client";

import logoUrl from "./../assets/_e9143fb5-fffe-41b2-b320-9b00cc908ff5.jpeg";

import { Link } from "react-router-dom";
import { StickyHeader } from "./StickyHeader.tsx";
import { HamburgerTwo } from "./HamburgerTwo.tsx";
import { UserMenu } from "./UserMenu.tsx";

import React, { MouseEvent, useEffect, useState } from "react";
import { cn } from "../lib/utils.ts";
import { UserDropdown } from "./UserDropdown.tsx";

type HeaderProps = {
  isLoggedIn?: boolean;
  logout?: () => void;
  userName?: string;
};

type MenuItem = { name: string; href: string };

export const Header: React.FC<HeaderProps> = ({ isLoggedIn, logout, userName }) => {
  const menuItems: MenuItem[] = [
    { name: "Home", href: "/" },
    { name: "Play", href: "/play" },
  ];

  const loggedInMenuItems: MenuItem[] = [{ name: "Profile", href: "/profile" }];

  if (isLoggedIn) {
    menuItems.push(...loggedInMenuItems);
  }

  const [isMenuClicked, setIsMenuClicked] = useState<boolean>(false);

  const toggleMenu = (): void => {
    // Store previous scroll
    const scrollY = document.body.style.top;

    // Set current scroll
    document.body.style.top = isMenuClicked ? "" : `-${window.scrollY}px`;

    // prevent scroll
    document.body.style.position = isMenuClicked ? "" : "fixed";
    document.body.style.overflowX = isMenuClicked ? "" : "hidden";

    // scroll back to original position
    isMenuClicked && window.scrollTo(0, parseInt(scrollY || "0") * -1);

    setIsMenuClicked(!isMenuClicked);
  };

  type ButtonHoverState = {
    left: number;
    height: number;
    width: number;
    duration: number;
    opacity: 1 | 0;
  };

  const [menuButtonHoverState, setMenuButtonHoverState] = useState<ButtonHoverState>({
    left: 0,
    height: 0,
    width: 0,
    duration: 0,
    opacity: 0,
  });

  const onButtonMouseEnter = (event: MouseEvent<HTMLAnchorElement>): void => {
    const target: HTMLButtonElement = event.target as HTMLButtonElement;

    setMenuButtonHoverState({
      left: target.offsetLeft,
      height: target.offsetHeight,
      width: target.offsetWidth,
      duration: 150 * menuButtonHoverState.opacity,
      opacity: 1,
    });
  };

  const onMenuMouseLeave = (): void => {
    setMenuButtonHoverState({
      left: 0,
      height: 0,
      width: 0,
      duration: 0,
      opacity: 0,
    });
  };

  useEffect(() => {
    function handleResize() {
      if (window.innerWidth > 1024) {
        isMenuClicked && toggleMenu();
      }
    }

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [isMenuClicked]);

  return (
    <>
      <StickyHeader className="z-10 bg-white" isFixed={isMenuClicked}>
        <div className="flex h-1/2 w-full items-center justify-between px-8">
          <div className="flex h-full">
            <Link to={"/"} className="flex h-full items-center">
              <img className="mr-4 h-full" src={logoUrl} alt="logo" />
              <span className="mr-12 text-xl font-extrabold">HexHex</span>
            </Link>
            <div className="hidden items-center text-sm text-gray-600 lg:flex" onMouseLeave={onMenuMouseLeave}>
              <div
                className="absolute rounded-full bg-black transition-all duration-300"
                style={{
                  left: menuButtonHoverState.left + "px",
                  width: menuButtonHoverState.width + "px",
                  height: menuButtonHoverState.height + "px",
                  opacity: menuButtonHoverState.opacity,
                  transitionProperty: "width, left",
                  transitionTimingFunction: "cubic-bezier(0.4, 0, 0.2, 1)",
                  transitionDuration: menuButtonHoverState.duration + "ms",
                }}
              />
              {menuItems.map((item, index) => (
                <Link
                  className="text-black transition-colors hover:text-white"
                  style={{ transitionDuration: 150 * menuButtonHoverState.opacity + "ms" }}
                  key={item.name + index}
                  to={item.href}
                  onMouseEnter={onButtonMouseEnter}
                >
                  <button className="relative cursor-pointer rounded-full border-none px-4 py-2">{item.name}</button>
                </Link>
              ))}
            </div>
          </div>
          <UserMenu className="hidden lg:flex" isLoggedIn={isLoggedIn || false} logout={logout || (() => {})} />
          <div className="flex justify-self-end lg:hidden">
            {isLoggedIn ? (
              <UserDropdown toggleDropdown={toggleMenu} isActive={isMenuClicked} userName={userName} />
            ) : (
              <HamburgerTwo onHamburgerClick={toggleMenu} isActive={isMenuClicked} />
            )}
          </div>
        </div>
      </StickyHeader>
      <div className={cn("fixed bottom-0 left-0 right-0 top-16 z-10 bg-white px-6", isMenuClicked || "hidden")}>
        <UserMenu
          className="mb-5 flex flex-col"
          isLoggedIn={isLoggedIn || false}
          logout={logout || (() => {})}
          toggleMenu={toggleMenu}
        />
        <ul>
          {menuItems.map((item, index) => (
            <Link key={item.name + index * 123} to={item.href} onClick={() => setIsMenuClicked(!isMenuClicked)}>
              <li className="cursor-pointer border-b border-black/15 py-3 pl-5 leading-6 text-black hover:bg-gray-50">
                {item.name}
              </li>
            </Link>
          ))}
        </ul>
      </div>
    </>
  );
};
