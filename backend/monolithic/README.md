# Ultracar - Processo seletivo

## Pilha de tecnologias do projeto
- **Java e Spring Boot**: Desenvolvimento de backend com APIs RESTful.
- **React**: Desenvolvimento de frontend para fornecer interface de usuário e interatividade.
- **PostgreSQL**: Gerenciamento de banco de dados para armazenar dados do usuário e registros da aplicação.
- **Testcontainers**: Desenvolvimento de testes de integração rigorosos para garantir confiabilidade e funcionalidade do código.
- **Spring Security**: Proteger as APIs e gerenciar a autenticação e autorização de usuário.
- **Chakra UI**:  Criação de componentes de interface de usuário.
- **React-pdf**:  Criação de PDFs.

## Pontos fortes
- **Desenvolvimento backend**:  Um dos principais destaques desse projeto é o desenvolvimento backend com Java e Spring Boot, resultando em uma lógica funcional e sólida para este projeto. Lógica para autenticação e autorização de usuário robustas, permitindo que apenas os usuários autorizados acessem as funcionalidades. Os testes de integração fornecem confiabilidade de que as funcionalidades do sistema estão funcionando, simulando cenários próximos a realidade.
- **Interface de usuário**: Optei pela utilização da biblioteca Chakra UI para facilitar o desenvolvimento da interface de usuário, apesar de não ter muita experiência com a biblioteca, o resultado foi bastante satisfatório.
## Pontos fracos
- **Familiaridade com react-pdf**: Uma das áreas em que enfrentei desafios foi com a biblioteca react-pdf. Como era minha primeira interação com essa tecnologia, tive algumas incertezas sobre a implementação da funcionalidade de geração de PDF. 
- **Ausência de testes unitários**: Infelizmente, o código atual não possui testes unitários. Embora o código possua testes de integração, reconheço a importância dos testes unitários para garantir que cada unidade do código funcione conforme esperado. Devido ao tempo limitado, priorizei outras áreas críticas do projeto e não pude desenvolvê-los.
- **Limitações da cobertura de testes de integração**: Os testes de integração implementados até o momento se concentram principalmente em verificar se as funcionalidades estão funcionando conforme o esperado. No entanto, reconheço que há uma lacuna na cobertura de testes, especialmente em relação aos cenários extremos (edge-cases) e ao tratamento de erros (error handling). Estou ciente que estas áreas são críticas para garantir a robustez e a confiabilidade da aplicação.
- **Desafios no frontend**: O desenvolvimento do frontend levou mais tempo do que o esperado pois estava há um tempo sem utilizar React e JavaScript. Durante o processo, percebi que a classe OrderOfService.jsx ficou maior do que deveria, tornando-se mais complexa e difícil de manter. Reconheço que o uso de Context API poderia aprimorar significativamente a legibilidade e a manutenção do código, facilitando a gestão do estado da aplicação de forma mais eficiente e estruturada. 
## Conclusão
- Apesar das dificuldades encontradas ao longo do desenvolvimento, estou satisfeito com o resultado alcançado neste projeto. Conseguir implementar eficientemente as funcionalidades solicitadas foi uma conquista gratificante. 

## Executar o projeto localmente

### Pré-requisitos
- JDK 17 ou superior
- Node.js
- Docker para executar o PostgreSQL em um contêiner e rodar os testes de integração

Para executar o projeto localmente, siga estas etapas:

1. Clone este repositório e construa o projeto.
2. Execute o seguinte comando na raiz do projeto para iniciar uma instância em execução do PostgreSQL:
```bash
   docker compose up
```
3.  No diretório raiz do projeto, instale as dependências do React com o seguinte comando:

```bash
cd client && npm install
```
4.  No diretório /client do projeto, inicie o backend e o frontend simultaneamente com o seguinte comando:

```bash
npm start
```

Este comando iniciará o frontend React e o backend Java, permitindo que você interaja com a aplicação acessando-a em seu navegador da web.

5. Após executar com sucesso a aplicação, você deverá ver mensagens de log indicando o início da aplicação. Os logs exibirão a porta na qual a aplicação está em execução. Além disso, certifique-se de que o frontend React também esteja em execução, acessando-o em seu navegador da web no endereço especificado.

6. A aplicação cria uma conta com o nome 'admin' e a senha 'senha' ao iniciar, você pode usar essas credenciais para se logar. Além disso, o banco de dados é populado ao iniciar pela classe DataLoader, o que permite testar a aplicação imediatamente com dados pré-existentes.

## Fotos do projeto
#### Nota: As capturas de tela da página inteira sofrem pequenas alterações em relação a aparência real da aplicação devido ao esticamento da imagem.

### Página de autenticação
![localhost_3000_(Nest Hub Max) (2)](https://github.com/DiegoBP02/ultracar/assets/103163622/4939ae0d-fa3c-4e50-94eb-492bf36f8791)

### Página principal
#### (Interface depois de procurar o cliente e seus veículos)
![localhost_3000_dashboard(Nest Hub Max) (8)](https://github.com/DiegoBP02/ultracar/assets/103163622/d34b6574-3e7a-4717-bfd0-1e9d7188fada)

### Página de criação da ordem de serviço
![localhost_3000_orderOfService_08e28d01-c88a-4213-b685-ad75611b4e70(Nest Hub Max)](https://github.com/DiegoBP02/ultracar/assets/103163622/432223b4-6a93-4dc1-a8e6-111799dd6635)

### Página de geração e para baixar o PDF
![localhost_3000_generatePdf_b5569f27-05b2-4569-a8f6-56cbdbd25a2c(Nest Hub Max)](https://github.com/DiegoBP02/ultracar/assets/103163622/1897dcb6-492c-4303-a029-97af0c56da76)

