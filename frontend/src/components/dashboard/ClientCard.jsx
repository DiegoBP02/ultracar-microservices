import {
  Box,
  Button,
  Center,
  Flex,
  Heading,
  Stack,
  Tag,
  Text,
  useColorModeValue,
} from "@chakra-ui/react";
import VehicleCard from "./VehicleCard";
import { useState } from "react";

export default function ClientCard({
  name,
  cpf,
  email,
  phone,
  address,
  vehicles,
}) {
  const [showCars, setShowCars] = useState(false);

  return (
    <Box>
      <Center py={6}>
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
              <Tag borderRadius={"full"}>{cpf}</Tag>
              <Heading fontSize={"2xl"} fontWeight={500} fontFamily={"body"}>
                {name}
              </Heading>
              <Text color={"gray.500"}>{email}</Text>
              <Text color={"gray.500"}>Phone {phone}</Text>
              <Text color={"gray.500"}>Endereço {address}</Text>
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
                onClick={() => setShowCars(!showCars)}
              >
                {showCars
                  ? "Ocultar veículos do cliente"
                  : "Procurar veículos do cliente"}
              </Button>
            </Stack>
          </Box>
        </Box>
      </Center>
      {showCars ? (
        <Flex>
          {vehicles.map((vehicle, index) => (
            <VehicleCard
              licensePlate={vehicle.licensePlate}
              model={vehicle.model}
              year={vehicle.year}
              accessories={vehicle.accessories}
              id={vehicle.id}
              key={index}
            />
          ))}
        </Flex>
      ) : null}
    </Box>
  );
}
