function statistics()

fileID = fopen('statistics.txt','r');
formatSpec = '%f:%f:%f:%f';
splittedValues = fscanf(fileID,formatSpec);
n = floor(size(splittedValues)/4);


for i=0:n-1
    rimeScores(i+1) = splittedValues(i*4+1);
    rithmScores(i+1) = splittedValues(i*4+2);
    semanticScores(i+1) = splittedValues(i*4+3);
    domainScores(i+1) = splittedValues(i*4+4);
end

x = 1:n-1;

hold on;
plot(x,rimeScores(2:n));
plot(x,rithmScores(2:n));
plot(x,semanticScores(2:n));
plot(x,domainScores(2:n));
plot(x,(domainScores(2:n)+semanticScores(2:n)+rithmScores(2:n)+rimeScores(2:n))/4,'Linewidth', 3);
end