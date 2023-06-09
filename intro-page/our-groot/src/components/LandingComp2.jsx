// import React from "react";
import styled from "styled-components";
import { useEffect } from "react";
import AOS from "aos";
import comp2_1 from "../assets/comp2_1.png";
import comp2_2 from "../assets/comp2_2.png";
// import { styled as muistyled } from "@mui/material/styles";
// import Button, { ButtonProps } from "@mui/material/Button";
// import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
// import KeyboardDoubleArrowDownIcon from "@mui/icons-material/KeyboardDoubleArrowDown";
// import abc_img from "/assets/img/LANDING2-3.png";

const LandingComp2 = () => {
  useEffect(() => {
    AOS.init();
  });

  return (
    <Background style={{ textAlign: "left" }}>
      <TitleDescriptionWrapper>
        <Content
          data-aos="fade-right"
          data-aos-delay="500"
          data-aos-duration="2500"
        >
          <StyledTitle>AI 식물 식별</StyledTitle>
          <ContentTitle>
            <ContentDescription>
              사진으로 간단하게
              <br />
              화분 등록
            </ContentDescription>
          </ContentTitle>
          <ContentScript>
            <StyledDescription>
              식물을 촬영하면 식물 식별부터
              <br /> 관련 정보까지 한번에
            </StyledDescription>
          </ContentScript>
        </Content>
        <ImgWrapper>
          <CustomedImage
            data-aos="fade-up"
            data-aos-delay="500"
            data-aos-duration="2500"
            src={comp2_1}
            style={{ width: "300px", top: "5vh", left: "10vw", zIndex: 2 }}
          ></CustomedImage>
          <CustomedImage
            data-aos="fade-up"
            data-aos-delay="500"
            data-aos-duration="1500"
            src={comp2_2}
            style={{ width: "300px", top: "15vh", left: "26vw", zIndex: 3 }}
          ></CustomedImage>
          {/* <CustomedImage
            data-aos="fade-up"
            data-aos-delay="1000"
            data-aos-duration="1500"
            // src={shiba3}
            style={{ width: "500px", top: "40vh", left: "15vw", zIndex: 4 }}
          ></CustomedImage>
          <CustomedImage
            data-aos="fade-up"
            data-aos-delay="1500"
            data-aos-duration="1500"
            // src={shiba4}
            style={{ width: "300px", top: "47vh", left: "35vw", zIndex: 5 }}
          ></CustomedImage> */}
        </ImgWrapper>
      </TitleDescriptionWrapper>
    </Background>
  );
};

export default LandingComp2;

const Background = styled.div`
  background: #f8f8f8;
  z-index: 0;
  height: 100vh;
`;

const TitleDescriptionWrapper = styled.div`
  top: 10em;
  padding-top: 10vh;
  margin-bottom: 30em;
  padding-left: 17vb;
  display: flex;
`;

const StyledTitle = styled.h2`
  font-family: "One-Mobile-POP";
  padding-top: 3em;
  padding-bottom: 20px;
  margin-top: 20px;
  color: #639a67;
  font-size: xx-large;
`;

const ContentTitle = styled.h2`
  font-family: "One-Mobile-POP";
  font-size: x-large;
`;

const ContentDescription = styled.h2``;

const StyledDescription = styled.h2`
  width: "10px";
  font-family: "ONE-Mobile-Regular";
  color: #828282;
`;

const ImgWrapper = styled.div`
  position: relative;
  width: 500px;
`;

const CustomedImage = styled.img`
  position: absolute;
  top: 15em;
  left: 15em;
  width: 500px;
`;

const Content = styled.div`
  padding-left: 10vw;
  padding-top: 20vh;
`;

const ContentScript = styled.div``;
