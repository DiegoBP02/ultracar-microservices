import {
  Box,
  Button,
  Center,
  Heading,
  Stack,
  Tag,
  Text,
  useColorModeValue,
} from "@chakra-ui/react";
import { useRef } from "react";
import { useNavigate } from "react-router-dom";

export default function VehicleCard({
  licensePlate,
  model,
  year,
  accessories,
  id,
}) {
  const navigate = useNavigate();

  return (
    <Box
      maxW={"300px"}
      minW={"300px"}
      w={"full"}
      m={2}
      bg={useColorModeValue("white", "gray.800")}
      boxShadow={"lg"}
      rounded={"md"}
      overflow={"hidden"}
    >
      <Box p={6}>
        <Stack spacing={2} align={"center"} mb={5}>
          <Tag borderRadius={"full"}>{licensePlate}</Tag>
          <Heading fontSize={"2xl"} fontWeight={500} fontFamily={"body"}>
            {model}
          </Heading>
          <Text color={"gray.500"}>{year}</Text>
          {accessories && accessories.length > 0 ? (
            <Text>
              {accessories.map((accessory, index) => (
                <span key={index}>
                  {accessory}
                  {index !== accessories.length - 1 && ", "}
                </span>
              ))}
            </Text>
          ) : (
            <Text color={"gray.500"}>Nenhum acessório disponível</Text>
          )}
          <Button
            bg={"red.400"}
            color={"white"}
            rounded={"full"}
            _hover={{
              transform: "translateY(-2px)",
              boxShadow: "lg",
            }}
            _focus={{
              bg: "green.500",
            }}
            onClick={() => navigate(`/orderOfService/${id}`)}
          >
            Criar ordem
          </Button>
        </Stack>
      </Box>
    </Box>
  );
}
